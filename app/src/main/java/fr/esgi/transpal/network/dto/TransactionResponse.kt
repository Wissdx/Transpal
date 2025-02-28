package fr.esgi.transpal.network.dto

data class TransactionResponse(
    val id: Int,
    val senderId: Int,
    val receiverId: Int,
    val amount: Double,
    val currency: String = "€",
    val type: String,
    val updatedAt: String,
    val createdAt: String,
    val message: String? = null
)