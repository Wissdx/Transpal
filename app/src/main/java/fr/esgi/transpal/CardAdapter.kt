package fr.esgi.transpal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.esgi.transpal.network.dto.CardModel


class CardAdapter(private val cardList: List<CardModel>) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardNumber: TextView = itemView.findViewById(R.id.card_number)
        val cardHolder: TextView = itemView.findViewById(R.id.card_holder)
        val cardExpiry: TextView = itemView.findViewById(R.id.card_expiry)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_credit_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cardList[position]
        holder.cardNumber.text = "**** **** **** " + card.cardNumber.takeLast(4) // Avoir l'effet de censure sur le num des cartes
        holder.cardHolder.text = card.cardHolder
        holder.cardExpiry.text = card.expiryDate
    }

    override fun getItemCount(): Int = cardList.size
}
