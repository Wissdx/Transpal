package fr.esgi.transpal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import fr.esgi.transpal.network.dto.TransactionResponse
import android.content.Context

class TransactionAdapter(
    private val transactions: List<TransactionResponse>,
    private val context: Context,
    private val userId: Int
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val transactionAmount: TextView = itemView.findViewById(R.id.transaction_amount)
        val transactionDate: TextView = itemView.findViewById(R.id.transaction_date)
        val transactionType: TextView = itemView.findViewById(R.id.transaction_type)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]


        val isOutgoing = when (transaction.type.lowercase()) {
            "withdraw", "send" -> transaction.senderId == userId
            "add_fund" -> false
            else -> transaction.amount < 0
        }

        val textColor = if (isOutgoing) R.color.red else R.color.green
        holder.transactionAmount.setTextColor(ContextCompat.getColor(context, textColor))

        val formattedAmount = if (isOutgoing) {
            "- %.2f %s".format(Math.abs(transaction.amount), transaction.currency)
        } else {
            "+ %.2f %s".format(transaction.amount, transaction.currency)
        }

        holder.transactionAmount.text = formattedAmount
        holder.transactionDate.text = transaction.createdAt
        holder.transactionType.text = transaction.type
    }

    override fun getItemCount(): Int = transactions.size
}
