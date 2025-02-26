package fr.esgi.transpal.network.repositories

import fr.esgi.transpal.network.RetrofitClient
import fr.esgi.transpal.network.dto.LoginRequest
import fr.esgi.transpal.network.dto.LoginResponse
import fr.esgi.transpal.network.dto.RegisterRequest
import fr.esgi.transpal.network.dto.RegisterResponse
import fr.esgi.transpal.network.services.AuthService
import retrofit2.Response

class AuthRepository {

    private val authService = RetrofitClient.instance.create(AuthService::class.java)

     suspend fun login(loginRequest: LoginRequest): LoginResponse {
        return authService.login(loginRequest)
    }

    suspend fun register(registerRequest: RegisterRequest): RegisterResponse {
        return authService.register(registerRequest)
    }

}