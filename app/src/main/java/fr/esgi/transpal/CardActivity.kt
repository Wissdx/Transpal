package fr.esgi.transpal

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import fr.esgi.transpal.network.dto.AddCardRequest
import fr.esgi.transpal.network.dto.CardModel
import fr.esgi.transpal.network.repositories.CardRepository
import fr.esgi.transpal.viewmodel.CardViewModel
import fr.esgi.transpal.viewmodel.factories.CardViewModelFactory

class CardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter
    private lateinit var addCardButton: Button
    private lateinit var saveCardButton: Button
    private lateinit var cancelCardButton: Button
    private lateinit var cardNumberInput: EditText
    private lateinit var cardHolderInput: EditText
    private lateinit var cardCvvInput: EditText
    private lateinit var cardExpiryInput: EditText
    private lateinit var userNameTextView: TextView
    private lateinit var cardDisplayContainer: RelativeLayout
    private lateinit var addCardFormContainer: RelativeLayout
    private val cardList = mutableListOf<CardModel>()

    private val cardViewModel: CardViewModel by viewModels {
        CardViewModelFactory(CardRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        recyclerView = findViewById(R.id.recyclerView)
        userNameTextView = findViewById(R.id.user_name)
        addCardButton = findViewById(R.id.add_card_button)
        saveCardButton = findViewById(R.id.save_card_button)
        cardNumberInput = findViewById(R.id.card_number_input)
        cardHolderInput = findViewById(R.id.card_holder_input)
        cardCvvInput = findViewById(R.id.card_cvv_input)
        cardExpiryInput = findViewById(R.id.card_expiry_input)
        cardDisplayContainer = findViewById(R.id.card_display_container)
        addCardFormContainer = findViewById(R.id.add_card_form_container)
        cancelCardButton = findViewById(R.id.cancel_card_button)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", "") ?: ""
        val userId = sharedPreferences.getInt("user_id", -1)
        val userName = sharedPreferences.getString("user_name", "") ?: ""

        if (token.isNotEmpty() && userId != -1) {
            userNameTextView.text = userName
            cardViewModel.getCards("Bearer $token", userId)
        }

        cardViewModel.cards.observe(this, Observer { cards ->
            cardList.clear()
            cardList.addAll(cards.map { CardModel(it.cardNumber, it.name, it.cvv, it.expiryDate) })
            updateUI()
        })

        addCardButton.setOnClickListener {
            cardDisplayContainer.visibility = View.GONE
            addCardFormContainer.visibility = View.VISIBLE
        }

        saveCardButton.setOnClickListener {
            // On retire les espaces pour la validation du numéro de carte
            val cardNumber = cardNumberInput.text.toString().replace(" ", "")
            val cardHolder = cardHolderInput.text.toString()
            val cardExpiry = cardExpiryInput.text.toString()
            val cardCvv = cardCvvInput.text.toString()

            if (validateCardDetails(cardNumber, cardHolder, cardExpiry, cardCvv)) {
                if (token.isNotEmpty() && userId != -1) {
                    val addCardRequest = AddCardRequest(
                        name = cardHolder,
                        cardNumber = cardNumber,
                        cvv = cardCvv,
                        expiryDate = cardExpiry
                    )

                    cardViewModel.addCard("Bearer $token", userId, addCardRequest, {
                        val newCard = CardModel(cardNumber, cardHolder, cardCvv, cardExpiry)
                        cardList.add(newCard)
                        updateUI()

                        cardDisplayContainer.visibility = View.VISIBLE
                        addCardFormContainer.visibility = View.GONE

                        cardNumberInput.text.clear()
                        cardHolderInput.text.clear()
                        cardExpiryInput.text.clear()
                        cardCvvInput.text.clear()

                        Toast.makeText(this, "Carte ajoutée avec succès !", Toast.LENGTH_SHORT).show()
                    }, { errorMessage ->
                        Toast.makeText(this, "Erreur lors de l'ajout de la carte : $errorMessage", Toast.LENGTH_SHORT).show()
                    })
                }
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs correctement", Toast.LENGTH_SHORT).show()
            }
        }

        cancelCardButton.setOnClickListener {
            cardDisplayContainer.visibility = View.VISIBLE
            addCardFormContainer.visibility = View.GONE

            cardNumberInput.text.clear()
            cardHolderInput.text.clear()
            cardExpiryInput.text.clear()
            cardCvvInput.text.clear()
        }

        val backButton = findViewById<LinearLayout>(R.id.back_button_container)
        backButton.setOnClickListener {
            finish()
        }

        val backButton2 = findViewById<LinearLayout>(R.id.back_button_container2)
        backButton2.setOnClickListener {
            cardDisplayContainer.visibility = View.VISIBLE
            addCardFormContainer.visibility = View.GONE

            cardNumberInput.text.clear()
            cardHolderInput.text.clear()
            cardExpiryInput.text.clear()
            cardCvvInput.text.clear()
        }

        setupTextWatchers()
    }

    private fun setupTextWatchers() {
        // TextWatcher pour le champ cardNumberInput : limitation à 16 chiffres et insertion d'un espace tous les 4 chiffres
        cardNumberInput.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return
                isFormatting = true

                val digitsOnly = s?.toString()?.replace(" ", "") ?: ""
                // Limiter à 16 chiffres
                val limited = if (digitsOnly.length > 16) digitsOnly.substring(0, 16) else digitsOnly

                val formatted = StringBuilder()
                for (i in limited.indices) {
                    if (i > 0 && i % 4 == 0) {
                        formatted.append(" ")
                    }
                    formatted.append(limited[i])
                }

                cardNumberInput.removeTextChangedListener(this)
                cardNumberInput.setText(formatted.toString())
                cardNumberInput.setSelection(formatted.length)
                cardNumberInput.addTextChangedListener(this)

                isFormatting = false
            }
        })

        // TextWatcher pour le champ cardExpiryInput : insertion automatique du slash uniquement si l'utilisateur a saisi plus de deux chiffres
        cardExpiryInput.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return
                isFormatting = true

                // On supprime d'abord tous les slash existants
                var text = s?.toString()?.replace("/", "") ?: ""
                // Si l'utilisateur a saisi plus de deux chiffres, on insère le slash après le 2ème caractère
                if (text.length > 2) {
                    text = text.substring(0, 2) + "/" + text.substring(2)
                }

                cardExpiryInput.removeTextChangedListener(this)
                cardExpiryInput.setText(text)
                cardExpiryInput.setSelection(text.length)
                cardExpiryInput.addTextChangedListener(this)

                isFormatting = false
            }
        })
    }

    private fun validateCardDetails(cardNumber: String, cardHolder: String, cardExpiry: String, cardCvv: String): Boolean {
        val cardNumberPattern = Regex("^[0-9]{16}$")
        val cardExpiryPattern = Regex("^(0[1-9]|1[0-2])/[0-9]{2}$")
        val cardCvvPattern = Regex("^[0-9]{3,4}$")

        return cardNumberPattern.matches(cardNumber) &&
                cardHolder.isNotEmpty() &&
                cardExpiryPattern.matches(cardExpiry) &&
                cardCvvPattern.matches(cardCvv)
    }

    private fun updateUI() {
        if (cardList.isEmpty()) {
            recyclerView.visibility = View.GONE
        } else {
            recyclerView.visibility = View.VISIBLE
            cardAdapter = CardAdapter(cardList)
            recyclerView.adapter = cardAdapter
        }
    }
}
