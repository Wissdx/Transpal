package fr.esgi.transpal.network.services

import fr.esgi.transpal.network.dto.BalanceResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface AccountService {

    @GET("accounts/{userId}/balance")
    suspend fun getBalance(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): BalanceResponse
}