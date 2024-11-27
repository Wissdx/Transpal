package fr.esgi.transpal.network.services

import fr.esgi.transpal.network.dto.LoginRequest
import fr.esgi.transpal.network.dto.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
}