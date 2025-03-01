package fr.esgi.transpal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.esgi.transpal.network.dto.ProfileRequest
import fr.esgi.transpal.network.dto.UserModel
import fr.esgi.transpal.network.repositories.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _users = MutableLiveData<List<UserModel>>()
    val users: LiveData<List<UserModel>> get() = _users

    private val _user = MutableLiveData<UserModel>()
    val user: LiveData<UserModel> get() = _user

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

    fun getUser(token: String, id: Int) {
        viewModelScope.launch {
            try {
                val user = userRepository.getUser(token, id)
                _user.postValue(user)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }

    fun updateUser(token: String, id: Int, profileRequest: ProfileRequest) {
        viewModelScope.launch {
            try {
                val user = userRepository.updateUser(token, id, profileRequest)
                _user.postValue(user)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }

    fun deleteUser(token: String, id: Int) {
        viewModelScope.launch {
            try {
                userRepository.deleteUser(token, id)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }
}