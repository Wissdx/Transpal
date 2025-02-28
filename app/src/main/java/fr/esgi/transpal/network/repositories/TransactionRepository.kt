package fr.esgi.transpal.network.repositories

import fr.esgi.transpal.network.RetrofitClient
import fr.esgi.transpal.network.dto.TransactionRequest
import fr.esgi.transpal.network.dto.TransactionResponse
import fr.esgi.transpal.network.services.TransactionService

class TransactionRepository {

    private val transactionService = RetrofitClient.instance.create(TransactionService::class.java)

    suspend fun sendMoney(token: String, transactionRequest: TransactionRequest): TransactionResponse {
        return transactionService.sendMoney("Bearer $token", transactionRequest)
    }
}