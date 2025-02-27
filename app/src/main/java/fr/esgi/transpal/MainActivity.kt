package fr.esgi.transpal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import fr.esgi.transpal.network.repositories.AccountRepository
import fr.esgi.transpal.viewmodel.AccountViewModel
import fr.esgi.transpal.viewmodel.factories.AccountViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var userImage: ImageView
    private lateinit var userName: TextView
    private lateinit var accountBalance: TextView

    private val accountRepository = AccountRepository(this)
    private val accountViewModel: AccountViewModel by viewModels {
        AccountViewModelFactory(accountRepository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userImage = findViewById<ImageView>(R.id.user_image)
        userName = findViewById<TextView>(R.id.user_name)
        accountBalance = findViewById(R.id.account_balance)

        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val userNameString = sharedPreferences.getString("user_name", "Utilisateur")
        val token = accountRepository.getAuthToken()
        val userId = accountRepository.getUserId()

        userName.text = userNameString

        if (token != null) {
            accountViewModel.getBalance(token, userId)
        }
        accountViewModel.balance.observe(this, { balance ->
            val responseText = balance.balance + " €"
            accountBalance.text = responseText
        })

        userImage.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        userName.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val financesSection = findViewById<RelativeLayout>(R.id.finances_section)
        financesSection.setOnClickListener {
            val intent = Intent(this, CardActivity::class.java)
            startActivity(intent)
        }
    }
}