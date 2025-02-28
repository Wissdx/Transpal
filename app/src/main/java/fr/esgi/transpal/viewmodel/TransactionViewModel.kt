package fr.esgi.transpal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.esgi.transpal.network.dto.TransactionRequest
import fr.esgi.transpal.network.dto.TransactionResponse
import fr.esgi.transpal.network.repositories.TransactionRepository
import kotlinx.coroutines.launch

class TransactionViewModel(private val transactionRepository: TransactionRepository) : ViewModel() {

    private val _transactionResponse = MutableLiveData<TransactionResponse>()
    val transactionResponse: LiveData<TransactionResponse> get() = _transactionResponse
    private val _transactionHistory = MutableLiveData<List<TransactionResponse>>()
    val transactionHistory: LiveData<List<TransactionResponse>> get() = _transactionHistory

    fun sendMoney(token: String, transactionRequest: TransactionRequest) {
        viewModelScope.launch {
            try {
                val response = transactionRepository.sendMoney(token, transactionRequest)
                _transactionResponse.postValue(response)
            } catch (e: Exception) {
                _transactionResponse.postValue(TransactionResponse(
                    id = -1,
                    senderId = transactionRequest.senderId,
                    receiverId = transactionRequest.receiverId,
                    amount = transactionRequest.amount,
                    currency = transactionRequest.currency,
                    type = "error",
                    updatedAt = "",
                    createdAt = "",
                    message = "Something went wrong. Make sure you have enough money in your account."
                ))
            }
        }
    }

    fun getTransactionHistory(token: String, userId: Int) {
        viewModelScope.launch {
            try {
                val response = transactionRepository.getTransactionHistory(token, userId)
                _transactionHistory.postValue(response)
            } catch (e: Exception) {
                _transactionHistory.postValue(
                    listOf(
                        TransactionResponse(
                            id = -1,
                            senderId = userId,
                            receiverId = userId,
                            amount = 0.0,
                            currency = "USD",
                            type = "error",
                            updatedAt = "",
                            createdAt = "",
                            message = "Something went wrong. Make sure you have enough money in your account."
                        )
                    )
                )
            }
        }
    }
}