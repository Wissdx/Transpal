package fr.esgi.transpal.network.repositories

import fr.esgi.transpal.network.RetrofitClient
import fr.esgi.transpal.network.dto.LoginRequest
import fr.esgi.transpal.network.dto.LoginResponse
import fr.esgi.transpal.network.services.AuthService
import retrofit2.Response

class AuthRepository {

    private val authService = RetrofitClient.instance.create(AuthService::class.java)

    fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        return authService.login(loginRequest).execute()
    }

}