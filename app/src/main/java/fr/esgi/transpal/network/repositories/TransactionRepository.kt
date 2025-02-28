package fr.esgi.transpal.network.repositories

import fr.esgi.transpal.network.RetrofitClient
import fr.esgi.transpal.network.dto.TransactionRequest
import fr.esgi.transpal.network.dto.TransactionResponse
import fr.esgi.transpal.network.dto.UserModel
import fr.esgi.transpal.network.services.TransactionService

class TransactionRepository {

    private val transactionService = RetrofitClient.instance.create(TransactionService::class.java)

    suspend fun sendMoney(token: String, transactionRequest: TransactionRequest): TransactionResponse {
        return transactionService.sendMoney("Bearer $token", transactionRequest)
    }

    suspend fun getTransactionHistory(token: String, userId: Int): List<TransactionResponse> {
        return transactionService.getTransactionHistory("Bearer $token", userId)
    }

    suspend fun getUsersSentMoneyTo(token: String, userId: Int): List<UserModel> {
        return transactionService.getUsersSentMoneyTo("Bearer $token", userId)
    }
}