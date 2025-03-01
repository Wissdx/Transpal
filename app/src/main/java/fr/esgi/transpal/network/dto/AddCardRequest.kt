package fr.esgi.transpal.network.dto

data class AddCardRequest(
    val type: String = "CARD",
    val name: String,
    val cardNumber: String,
    val cvv: String,
    val expiryDate: String
)