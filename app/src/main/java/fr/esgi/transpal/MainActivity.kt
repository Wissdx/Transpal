package fr.esgi.transpal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import fr.esgi.transpal.network.dto.CardModel
import fr.esgi.transpal.network.repositories.AccountRepository
import fr.esgi.transpal.viewmodel.AccountViewModel
import fr.esgi.transpal.viewmodel.factories.AccountViewModelFactory

class MainActivity : AppCompatActivity() {

    private val accountRepository = AccountRepository(this)
    private val accountViewModel: AccountViewModel by viewModels {
        AccountViewModelFactory(accountRepository)
    }

    private lateinit var accountBalance: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter
    private lateinit var addCardButton: TextView
    private lateinit var userImage: ImageView
    private lateinit var userName: TextView

    private val cardList = mutableListOf<CardModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        accountBalance = findViewById(R.id.account_balance)
        recyclerView = findViewById(R.id.recyclerView)
        addCardButton = findViewById(R.id.add_card_button)
        userImage = findViewById(R.id.user_image)
        userName = findViewById(R.id.user_name)

        val userName = findViewById<TextView>(R.id.user_name)

        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val userNameString = sharedPreferences.getString("user_name", "Utilisateur")
        userName.text = userNameString

        val token = accountRepository.getAuthToken()
        val userId = accountRepository.getUserId()

        if (token != null && userId != -1) {
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

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        cardList.add(CardModel("1234567812345678", "Dark Vador", "12/24"))
        cardList.add(CardModel("5834865414872912", "Obi-Wan Kenobi", "01/26"))

        updateUI()

        addCardButton.setOnClickListener {
            val intent = Intent(this, CardActivity::class.java)
            startActivity(intent)
        }
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