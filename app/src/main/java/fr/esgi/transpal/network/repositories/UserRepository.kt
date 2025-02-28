package fr.esgi.transpal.network.repositories

import fr.esgi.transpal.network.RetrofitClient
import fr.esgi.transpal.network.dto.UserModel
import fr.esgi.transpal.network.services.UserService

class UserRepository {

    private val userService = RetrofitClient.instance.create(UserService::class.java)

    suspend fun getUsers(token: String): List<UserModel> {
        return userService.getUsers("Bearer $token")
    }
}