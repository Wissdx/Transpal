package fr.esgi.transpal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.esgi.transpal.network.dto.TransactionResponse

class TransactionAdapter(private val transactions: List<TransactionResponse>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

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
        holder.transactionAmount.text = "${transaction.amount} ${transaction.currency}"
        holder.transactionDate.text = transaction.createdAt
        holder.transactionType.text = transaction.type
    }

    override fun getItemCount(): Int = transactions.size
}