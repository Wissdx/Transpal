package fr.esgi.transpal.network.repositories

import android.content.Context
import fr.esgi.transpal.network.RetrofitClient
import fr.esgi.transpal.network.dto.BalanceResponse
import fr.esgi.transpal.network.services.AccountService

class AccountRepository(private val context: Context) {

    private val accountService = RetrofitClient.instance.create(AccountService::class.java)

    suspend fun getBalance(token: String, userId: Int): BalanceResponse {
        return accountService.getBalance("Bearer $token", userId)
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