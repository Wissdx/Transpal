package fr.esgi.transpal

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.esgi.transpal.network.repositories.TransactionRepository
import fr.esgi.transpal.viewmodel.TransactionViewModel
import fr.esgi.transpal.viewmodel.factories.TransactionViewModelFactory

class TransactionHistoryActivity : AppCompatActivity() {

    private lateinit var transactionsRecyclerView: RecyclerView
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var backTextView: TextView
    private lateinit var backImageView: ImageView

    private val transactionRepository = TransactionRepository()
    private val transactionViewModel: TransactionViewModel by viewModels {
        TransactionViewModelFactory(transactionRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_history)

        transactionsRecyclerView = findViewById(R.id.transactions_recycler_view)
        backTextView = findViewById(R.id.back_here_tv)
        backImageView = findViewById(R.id.back_icon)

        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)
        val token = sharedPreferences.getString("auth_token", "") ?: ""

        if (token.isNotEmpty() && userId != -1) {
            transactionViewModel.getTransactionHistory(token, userId)
        }

        transactionViewModel.transactionHistory.observe(this, { transactions ->
            if (transactions.isNotEmpty()) {
                val recentTransactions = transactions.sortedByDescending { it.createdAt }
                transactionAdapter = TransactionAdapter(recentTransactions)
                transactionsRecyclerView.layoutManager = LinearLayoutManager(this)
                transactionsRecyclerView.adapter = transactionAdapter
            } else {
                Toast.makeText(this, "No transactions found", Toast.LENGTH_SHORT).show()
            }
        })

        backTextView.setOnClickListener { finish() }
        backImageView.setOnClickListener { finish() }

    }
}