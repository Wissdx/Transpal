package fr.esgi.transpal.network.services

import fr.esgi.transpal.network.dto.TransactionRequest
import fr.esgi.transpal.network.dto.TransactionResponse
import fr.esgi.transpal.network.dto.UserModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface TransactionService {

    @POST("transactions/send")
    suspend fun sendMoney(
        @Header("Authorization") token: String,
        @Body transactionRequest: TransactionRequest
    ): TransactionResponse

    @GET("transactions/{userId}/history")
    suspend fun getTransactionHistory(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): List<TransactionResponse>

    @GET("transactions/{userId}/sent-to")
    suspend fun getUsersSentMoneyTo(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): List<UserModel>
}