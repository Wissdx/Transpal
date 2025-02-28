package fr.esgi.transpal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.esgi.transpal.network.dto.UserModel
import fr.esgi.transpal.network.repositories.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _users = MutableLiveData<List<UserModel>>()
    val users: LiveData<List<UserModel>> get() = _users

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun getUsers(token: String) {
        viewModelScope.launch {
            try {
                val users = userRepository.getUsers(token)
                _users.postValue(users)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }
}