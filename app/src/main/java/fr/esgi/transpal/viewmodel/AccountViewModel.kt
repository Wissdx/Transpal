package fr.esgi.transpal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.esgi.transpal.network.dto.BalanceRequest
import fr.esgi.transpal.network.dto.BalanceResponse
import fr.esgi.transpal.network.dto.ProfileRequest
import fr.esgi.transpal.network.dto.ProfileResponse
import fr.esgi.transpal.network.repositories.AccountRepository
import fr.esgi.transpal.network.repositories.ProfileRepository
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
}