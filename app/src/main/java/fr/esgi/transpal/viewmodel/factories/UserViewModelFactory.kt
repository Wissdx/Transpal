package fr.esgi.transpal.viewmodel.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.esgi.transpal.network.repositories.UserRepository
import fr.esgi.transpal.viewmodel.UserViewModel

class UserViewModelFactory(userRepository: UserRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(UserRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}