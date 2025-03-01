package fr.esgi.transpal.network.dto

data class CardResponse(
    val id: Int,
    val type: String,
    val name: String,
    val cardNumber: String,
    val cvv: String,
    val expiryDate: String,
    val createdAt: String,
    val updatedAt: String,
    val userId: Int
)