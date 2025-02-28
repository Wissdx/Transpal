package fr.esgi.transpal.network.dto

data class TransactionRequest(
    val senderId: Int,
    val receiverId: Int,
    val amount: Double,
    val currency: String = "€"
)