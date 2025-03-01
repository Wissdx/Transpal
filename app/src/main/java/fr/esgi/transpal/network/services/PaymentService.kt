package fr.esgi.transpal.network.services

import fr.esgi.transpal.network.dto.AddCardRequest
import fr.esgi.transpal.network.dto.CardResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentService {

    @POST("payment-methods/{userId}/add")
    suspend fun addCard(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int,
        @Body addCardRequest: AddCardRequest
    )

    @GET("payment-methods/{userId}")
    suspend fun getCards(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): List<CardResponse>
}