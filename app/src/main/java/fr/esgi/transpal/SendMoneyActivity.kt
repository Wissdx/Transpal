package fr.esgi.transpal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import fr.esgi.transpal.network.RetrofitClient
import fr.esgi.transpal.network.dto.TransactionRequest
import fr.esgi.transpal.network.repositories.TransactionRepository
import fr.esgi.transpal.viewmodel.TransactionViewModel
import fr.esgi.transpal.viewmodel.factories.TransactionViewModelFactory

class SendMoneyActivity : AppCompatActivity() {

    private lateinit var receiverIdEt: EditText
    private lateinit var amountEt: EditText
    private lateinit var sendButton: Button
    private lateinit var backTextView: TextView
    private lateinit var backImageView: ImageView
    private lateinit var userInfoLayout: LinearLayout
    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView

    private val transactionRepository = TransactionRepository()
    private val transactionViewModel: TransactionViewModel by viewModels {
        TransactionViewModelFactory(transactionRepository)
    }

    private val userSelectionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedUserId = result.data?.getIntExtra("selectedUserId", -1)
            val selectedUserName = result.data?.getStringExtra("selectedUserName")
            val selectedUserEmail = result.data?.getStringExtra("selectedUserEmail")

            if (selectedUserId != null && selectedUserId != -1) {
                receiverIdEt.setText(selectedUserId.toString())
                userNameTextView.text = selectedUserName
                userEmailTextView.text = selectedUserEmail
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_money)

        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val senderId = sharedPreferences.getInt("user_id", -1)
        val token = sharedPreferences.getString("auth_token", "") ?: ""

        receiverIdEt = findViewById(R.id.receiver_id_et)
        amountEt = findViewById(R.id.amount_et)
        sendButton = findViewById(R.id.send_button)
        backTextView = findViewById(R.id.back_here_tv)
        backImageView = findViewById(R.id.back_icon)
        userInfoLayout = findViewById(R.id.user_info)
        userNameTextView = findViewById(R.id.user_name)
        userEmailTextView = findViewById(R.id.user_email)

        val selectedUserId = intent.getIntExtra("selectedUserId", -1)
        val selectedUserName = intent.getStringExtra("selectedUserName")
        val selectedUserEmail = intent.getStringExtra("selectedUserEmail")

        if (selectedUserId != -1) {
            receiverIdEt.setText(selectedUserId.toString())
            userNameTextView.text = selectedUserName
            userEmailTextView.text = selectedUserEmail
        }

        userInfoLayout.setOnClickListener {
            val intent = Intent(this, UserSelectionActivity::class.java)
            userSelectionLauncher.launch(intent)
        }

        sendButton.setOnClickListener {
            val receiverIdText = receiverIdEt.text.toString()
            val amountText = amountEt.text.toString()

            // On epmeche l'envoi si aucun destinatairen'est choisi
            if (receiverIdText.isEmpty()) {
                Toast.makeText(this, "Veuillez sélectionner un destinataire", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // On verif si montant est saisi
            if (amountText.isEmpty()) {
                Toast.makeText(this, "Veuillez entrer un montant", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val receiverId = receiverIdText.toIntOrNull()
            val amount = amountText.toDoubleOrNull()

            // On ctonrole si receiverId est bien un nombre valide
            if (receiverId == null || receiverId == -1) {
                Toast.makeText(this, "Destinataire invalide", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // On controle si le montant est un nombre valide et supérieur à 0
            if (amount == null || amount <= 0) {
                Toast.makeText(this, "Veuillez entrer un montant valide", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val transactionRequest = TransactionRequest(senderId, receiverId, amount, "€")
            transactionViewModel.sendMoney(token, transactionRequest)
        }


        backTextView.setOnClickListener {
            finish()
        }

        backImageView.setOnClickListener {
            finish()
        }

        transactionViewModel.transactionResponse.observe(this, { result ->
            if (result.type != "error") {
                Toast.makeText(this, "Transaction effectuée avec succès", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Erreur: ${result.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}