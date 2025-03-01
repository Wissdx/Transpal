package fr.esgi.transpal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.esgi.transpal.network.dto.AddFundsRequest
import fr.esgi.transpal.network.dto.BalanceResponse
import fr.esgi.transpal.network.dto.WithdrawFundsRequest
import fr.esgi.transpal.network.repositories.AccountRepository
import kotlinx.coroutines.launch

class AccountViewModel(private val accountRepository: AccountRepository) : ViewModel() {

    private val _balance = MutableLiveData<BalanceResponse>()
    val balance: LiveData<BalanceResponse> = _balance

    fun getBalance(token: String, id: Int) {
        viewModelScope.launch {
            val balanceInfo = accountRepository.getBalance(token, id)
            _balance.value = balanceInfo
        }
    }

    fun addFunds(token: String, userId: Int, addFundsRequest: AddFundsRequest, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                accountRepository.addFunds(token, userId, addFundsRequest)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Erreur inconnue")
            }
        }
    }

    fun withdrawFunds(token: String, userId: Int, withdrawFundsRequest: WithdrawFundsRequest, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                accountRepository.withdrawFunds(token, userId, withdrawFundsRequest)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Erreur inconnue")
            }
        }
    }
}