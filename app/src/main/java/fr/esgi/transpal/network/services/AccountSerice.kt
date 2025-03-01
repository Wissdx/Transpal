package fr.esgi.transpal.network.services

import fr.esgi.transpal.network.dto.AddFundsRequest
import fr.esgi.transpal.network.dto.BalanceResponse
import fr.esgi.transpal.network.dto.WithdrawFundsRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AccountService {

    @GET("accounts/{userId}/balance")
    suspend fun getBalance(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): BalanceResponse

    @POST("accounts/{userId}/add-funds")
    suspend fun addFunds(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int,
        @Body addFundsRequest: AddFundsRequest
    )

    @POST("accounts/{userId}/withdraw")
    suspend fun withdrawFunds(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int,
        @Body withdrawFundsRequest: WithdrawFundsRequest
    )
}