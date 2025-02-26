package fr.esgi.transpal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.esgi.transpal.network.dto.RegisterRequest
import fr.esgi.transpal.network.dto.RegisterResponse
import fr.esgi.transpal.network.repositories.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> get() = _registerResponse

    fun register(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            try {
                val response = authRepository.register(registerRequest)
                _registerResponse.value = response
            } catch (e: Exception) {
                _registerResponse.value = RegisterResponse(
                    -1, e.message ?: "Erreur réseau",
                    password = "",
                    name = "",
                    updatedAt = "",
                    createdAt = ""
                )
            }
        }
    }
}