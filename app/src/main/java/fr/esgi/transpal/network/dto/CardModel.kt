package fr.esgi.transpal.network.dto

data class CardModel(

        val cardNumber: String,
        val cardHolder: String,
        val cvv: String,
        val expiryDate: String
)
