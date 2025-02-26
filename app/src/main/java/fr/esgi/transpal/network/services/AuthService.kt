package fr.esgi.transpal.network.services

import fr.esgi.transpal.network.dto.LoginRequest
import fr.esgi.transpal.network.dto.LoginResponse
import fr.esgi.transpal.network.dto.RegisterRequest
import fr.esgi.transpal.network.dto.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse

}