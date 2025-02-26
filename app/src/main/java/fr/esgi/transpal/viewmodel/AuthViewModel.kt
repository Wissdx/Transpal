package fr.esgi.transpal.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.esgi.transpal.network.dto.LoginRequest
import fr.esgi.transpal.network.dto.LoginResponse
import fr.esgi.transpal.network.repositories.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> get() = _loginResponse

    fun login(loginRequest: LoginRequest) {
        Log.w("AuthViewModel", "login")
        viewModelScope.launch {
            try {
                val response = authRepository.login(loginRequest)
                if (response.isSuccessful) {
                    _loginResponse.value = response.body()
                } else {
                    Log.w("AuthViewModel", "Error : ${response.errorBody()?.string()}")
                    val errorMessage = response.errorBody()?.string()
                    _loginResponse.value = LoginResponse("", "An error occurred : $errorMessage")
                }
            } catch (e: Exception) {

                Log.w("AuthViewModel", "Error : ${e.message} : ${e.toString()}")
                _loginResponse.value = LoginResponse("", e.message ?: "An error occurred")
            }
        }
    }



}