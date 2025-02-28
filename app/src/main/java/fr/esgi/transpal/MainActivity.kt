package fr.esgi.transpal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import fr.esgi.transpal.network.dto.CardModel
import fr.esgi.transpal.network.repositories.AccountRepository
import fr.esgi.transpal.network.repositories.TransactionRepository
import fr.esgi.transpal.viewmodel.AccountViewModel
import fr.esgi.transpal.viewmodel.TransactionViewModel
import fr.esgi.transpal.viewmodel.factories.AccountViewModelFactory
import fr.esgi.transpal.viewmodel.factories.TransactionViewModelFactory

class MainActivity : AppCompatActivity() {

    private val accountRepository = AccountRepository(this)
    private val accountViewModel: AccountViewModel by viewModels {
        AccountViewModelFactory(accountRepository)
    }

    private val transactionRepository = TransactionRepository()
    private val transactionViewModel: TransactionViewModel by viewModels {
        TransactionViewModelFactory(transactionRepository)
    }

    private lateinit var accountBalance: TextView
    private lateinit var userName: TextView
    private lateinit var addCardButton: TextView
    private lateinit var seeMoreTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var transactionsRecyclerView: RecyclerView
    private lateinit var recentUsersRecyclerView: RecyclerView
    private lateinit var userImage: ImageView
    private lateinit var sendButton: Button
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    private lateinit var cardAdapter: CardAdapter
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var recentUserAdapter: RecentUserAdapter

    private val cardList = mutableListOf<CardModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        transactionsRecyclerView = findViewById(R.id.transactions_recycler_view)
        recyclerView = findViewById(R.id.recyclerView)
        recentUsersRecyclerView = findViewById(R.id.recent_users_recycler_view)

        accountBalance = findViewById(R.id.account_balance)
        addCardButton = findViewById(R.id.add_card_button)
        userImage = findViewById(R.id.user_image)
        userName = findViewById(R.id.user_name)
        sendButton = findViewById(R.id.button1)
        seeMoreTextView = findViewById(R.id.see_more_transaction)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        val userName = findViewById<TextView>(R.id.user_name)

        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val userNameString = sharedPreferences.getString("user_name", "Utilisateur")
        userName.text = userNameString

        val token = accountRepository.getAuthToken()
        val userId = accountRepository.getUserId()

        if (token != null && userId != -1) {
            accountViewModel.getBalance(token, userId)
            transactionViewModel.getTransactionHistory(token, userId)
            transactionViewModel.getUsersSentMoneyTo(token, userId)
        }

        accountViewModel.balance.observe(this, { balance ->
            val responseText = balance.balance + " €"
            accountBalance.text = responseText
        })

        transactionViewModel.transactionHistory.observe(this, { transactions ->
            if (transactions.isNotEmpty()) {
                val recentTransactions = transactions.takeLast(4)
                transactionAdapter = TransactionAdapter(recentTransactions)
                transactionsRecyclerView.layoutManager = LinearLayoutManager(this)
                transactionsRecyclerView.adapter = transactionAdapter
            } else {
                Toast.makeText(this, "No transactions found", Toast.LENGTH_SHORT).show()
            }
        })

        transactionViewModel.usersSentMoneyTo.observe(this, { users ->
            if (users.isNotEmpty()) {
                recentUserAdapter = RecentUserAdapter(this, users) {
                    val intent = Intent(this, SendMoneyActivity::class.java)
                    intent.putExtra("selectedUserId", it.id)
                    intent.putExtra("selectedUserName", it.name)
                    intent.putExtra("selectedUserEmail", it.email)
                    setResult(RESULT_OK, intent)
                    startActivity(intent)
                }
                recentUsersRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                recentUsersRecyclerView.adapter = recentUserAdapter
            } else {
                Toast.makeText(this, "No recent users found", Toast.LENGTH_SHORT).show()
            }
        })

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        cardList.add(CardModel("1234567812345678", "Dark Vador", "12/24"))
        cardList.add(CardModel("5834865414872912", "Obi-Wan Kenobi", "01/26"))

        updateUI()

        userImage.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        userName.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        addCardButton.setOnClickListener {
            val intent = Intent(this, CardActivity::class.java)
            startActivity(intent)
        }

        sendButton.setOnClickListener {
            val intent = Intent(this, SendMoneyActivity::class.java)
            startActivity(intent)
        }

        seeMoreTextView.setOnClickListener {
            val intent = Intent(this, TransactionHistoryActivity::class.java)
            startActivity(intent)
        }

        swipeRefreshLayout.setOnRefreshListener {
            refreshData(token, userId)
        }

        val handler = Handler(Looper.getMainLooper())
        val refreshRunnable = object : Runnable {
            override fun run() {
                refreshData(token, userId)
                handler.postDelayed(this, 5000)
            }
        }
        handler.post(refreshRunnable)
    }

    private fun refreshData(token: String?, userId: Int) {
        if (token != null && userId != -1) {
            accountViewModel.getBalance(token, userId)
            transactionViewModel.getTransactionHistory(token, userId)
            transactionViewModel.getUsersSentMoneyTo(token, userId)
        }
        swipeRefreshLayout.isRefreshing = false
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