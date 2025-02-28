package fr.esgi.transpal.network.services

import fr.esgi.transpal.network.dto.TransactionRequest
import fr.esgi.transpal.network.dto.TransactionResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface TransactionService {

    @POST("transactions/send")
    suspend fun sendMoney(
        @Header("Authorization") token: String,
        @Body transactionRequest: TransactionRequest
    ): TransactionResponse
}