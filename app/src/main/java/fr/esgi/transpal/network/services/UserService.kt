package fr.esgi.transpal.network.services

import fr.esgi.transpal.network.dto.ProfileRequest
import fr.esgi.transpal.network.dto.UserModel
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {

    @GET("users")
    suspend fun getUsers(@Header("Authorization") token: String,): List<UserModel>

    @GET("users/{id}")
    suspend fun getUser(@Header("Authorization") token: String, @Path("id") id: Int): UserModel

    @PUT("users/{id}")
    suspend fun updateUser(@Header("Authorization") token: String, @Path("id") id: Int, @Body profileRequest: ProfileRequest): UserModel

    @DELETE("users/{id}")
    suspend fun deleteUser(@Header("Authorization") token: String, @Path("id") id: Int)
}