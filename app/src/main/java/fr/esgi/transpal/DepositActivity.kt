package fr.esgi.transpal

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import fr.esgi.transpal.network.dto.AddFundsRequest
import fr.esgi.transpal.network.repositories.AccountRepository
import fr.esgi.transpal.network.repositories.CardRepository
import fr.esgi.transpal.viewmodel.AccountViewModel
import fr.esgi.transpal.viewmodel.CardViewModel
import fr.esgi.transpal.viewmodel.factories.AccountViewModelFactory
import fr.esgi.transpal.viewmodel.factories.CardViewModelFactory

class DepositActivity : AppCompatActivity() {

    private val accountRepository = AccountRepository(this)
    private val accountViewModel: AccountViewModel by viewModels {
        AccountViewModelFactory(accountRepository)
    }

    private val cardRepository = CardRepository()
    private val cardViewModel: CardViewModel by viewModels {
        CardViewModelFactory(cardRepository)
    }

    private lateinit var cardSpinner: Spinner
    private lateinit var amountEditText: EditText
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit)

        cardSpinner = findViewById(R.id.card_spinner)
        amountEditText = findViewById(R.id.amount_edit_text)
        addButton = findViewById(R.id.add_button)

        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", "") ?: ""
        val userId = sharedPreferences.getInt("user_id", -1)

        if (token.isNotEmpty() && userId != -1) {
            cardViewModel.getCards("Bearer $token", userId)
        }

        cardViewModel.cards.observe(this, { cards ->
            val cardNames = cards.map { "Card: " + it.name }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cardNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            cardSpinner.adapter = adapter
        })

        addButton.setOnClickListener {
            val amount = amountEditText.text.toString().toDoubleOrNull()
            if (amount != null && amount > 0) {
                val addFundsRequest = AddFundsRequest(amount)
                accountViewModel.addFunds(token, userId, addFundsRequest, {
                    Toast.makeText(this, "Fonds ajoutés avec succès !", Toast.LENGTH_SHORT).show()
                    finish()
                }, { errorMessage ->
                    Toast.makeText(this, "Erreur lors de l'ajout des fonds : $errorMessage", Toast.LENGTH_SHORT).show()
                })
            } else {
                Toast.makeText(this, "Veuillez entrer un montant valide", Toast.LENGTH_SHORT).show()
            }
        }


        val backButton = findViewById<LinearLayout>(R.id.back_button_container)
        backButton.setOnClickListener {
            finish()
        }

    }
}