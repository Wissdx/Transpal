package fr.esgi.transpal.viewmodel.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.esgi.transpal.network.repositories.AccountRepository
import fr.esgi.transpal.viewmodel.AccountViewModel

class AccountViewModelFactory(private val accountRepository: AccountRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            return AccountViewModel(accountRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}