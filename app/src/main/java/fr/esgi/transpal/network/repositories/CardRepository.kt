package fr.esgi.transpal.network.repositories

import fr.esgi.transpal.network.RetrofitClient
import fr.esgi.transpal.network.dto.AddCardRequest
import fr.esgi.transpal.network.dto.CardResponse
import fr.esgi.transpal.network.services.PaymentService

class CardRepository {

    private val paymentService = RetrofitClient.instance.create(PaymentService::class.java)

    suspend fun addCard(token: String, userId: Int, addCardRequest: AddCardRequest) {
        paymentService.addCard(token, userId, addCardRequest)
    }

    suspend fun getCards(token: String, userId: Int): List<CardResponse> {
        return paymentService.getCards(token, userId)
    }
}