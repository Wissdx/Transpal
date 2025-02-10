package fr.esgi.transpal

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import fr.esgi.transpal.network.dto.CardModel

class CardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter
    private lateinit var emptyCardPlaceholder: ImageView
    private lateinit var addCardButton: Button
    private lateinit var saveCardButton: Button
    private lateinit var cancelCardButton: Button
    private lateinit var cardNumberInput: EditText
    private lateinit var cardHolderInput: EditText
    private lateinit var cardExpiryInput: EditText
    private lateinit var cardDisplayContainer: RelativeLayout
    private lateinit var addCardFormContainer: RelativeLayout
    private val cardList = mutableListOf<CardModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        // Initialisation des vues
        recyclerView = findViewById(R.id.recyclerView)
        emptyCardPlaceholder = findViewById(R.id.empty_card_placeholder)
        addCardButton = findViewById(R.id.add_card_button)
        saveCardButton = findViewById(R.id.save_card_button)
        cardNumberInput = findViewById(R.id.card_number_input)
        cardHolderInput = findViewById(R.id.card_holder_input)
        cardExpiryInput = findViewById(R.id.card_expiry_input)
        cardDisplayContainer = findViewById(R.id.card_display_container)
        addCardFormContainer = findViewById(R.id.add_card_form_container)
        cancelCardButton = findViewById(R.id.cancel_card_button)

        // Initialisation du RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        // Ajout de cartes de test
        cardList.add(CardModel("1234567812345678", "Dark Vador", "12/24"))
        cardList.add(CardModel("5834865414872912", "Obi-Wan Kenobi", "01/26"))

        updateUI() // Vérifier l'affichage initial

        // AJOUT DE CARTES
        addCardButton.setOnClickListener {
            cardDisplayContainer.visibility = View.GONE
            addCardFormContainer.visibility = View.VISIBLE
        }

        // SAUVEGARDE CARTES
        saveCardButton.setOnClickListener {
            val cardNumber = cardNumberInput.text.toString()
            val cardHolder = cardHolderInput.text.toString()
            val cardExpiry = cardExpiryInput.text.toString()

            if (cardNumber.isNotEmpty() && cardHolder.isNotEmpty() && cardExpiry.isNotEmpty()) {
                val newCard = CardModel(cardNumber, cardHolder, cardExpiry)
                cardList.add(newCard)
                updateUI()

                // On raffiche les cartes et recache le formulaire
                cardDisplayContainer.visibility = View.VISIBLE
                addCardFormContainer.visibility = View.GONE

                // On vide le formulaire après l'ajout
                cardNumberInput.text.clear()
                cardHolderInput.text.clear()
                cardExpiryInput.text.clear()

                Toast.makeText(this, "Carte ajoutée avec succès !", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }
        }
        cancelCardButton.setOnClickListener {
            cardDisplayContainer.visibility = View.VISIBLE
            addCardFormContainer.visibility = View.GONE

            // On vide le formulaire
            cardNumberInput.text.clear()
            cardHolderInput.text.clear()
            cardExpiryInput.text.clear()
        }

        // Retour en arrière
        val backButton = findViewById<LinearLayout>(R.id.back_button_container)
        backButton.setOnClickListener {
            finish() //  Ferme l'activity et revient en arrière
        }
    }

    private fun updateUI() {
        if (cardList.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyCardPlaceholder.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyCardPlaceholder.visibility = View.GONE
            cardAdapter = CardAdapter(cardList)
            recyclerView.adapter = cardAdapter
        }
    }
}
