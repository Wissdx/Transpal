package fr.esgi.transpal.network.services

import fr.esgi.transpal.network.dto.UserModel
import retrofit2.http.GET
import retrofit2.http.Header

interface UserService {

    @GET("users")
    suspend fun getUsers(@Header("Authorization") token: String,): List<UserModel>
}