package fr.esgi.transpal.network.repositories

import android.content.Context
import fr.esgi.transpal.network.RetrofitClient
import fr.esgi.transpal.network.dto.AddFundsRequest
import fr.esgi.transpal.network.dto.BalanceResponse
import fr.esgi.transpal.network.dto.WithdrawFundsRequest
import fr.esgi.transpal.network.services.AccountService

class AccountRepository(private val context: Context) {

    private val accountService = RetrofitClient.instance.create(AccountService::class.java)

    suspend fun getBalance(token: String, userId: Int): BalanceResponse {
        return accountService.getBalance("Bearer $token", userId)
    }

    suspend fun addFunds(token: String, userId: Int, addFundsRequest: AddFundsRequest) {
        accountService.addFunds("Bearer $token", userId, addFundsRequest)
    }

    suspend fun withdrawFunds(token: String, userId: Int, withdrawFundsRequest: WithdrawFundsRequest) {
        accountService.withdrawFunds("Bearer $token", userId, withdrawFundsRequest)
    }

    fun getAuthToken(): String? {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }

    fun getUserId(): Int {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("user_id", -1)
    }
}