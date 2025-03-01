package fr.esgi.transpal.network.repositories

import fr.esgi.transpal.network.RetrofitClient
import fr.esgi.transpal.network.dto.ProfileRequest
import fr.esgi.transpal.network.dto.UserModel
import fr.esgi.transpal.network.services.UserService

class UserRepository {

    private val userService = RetrofitClient.instance.create(UserService::class.java)

    suspend fun getUsers(token: String): List<UserModel> {
        return userService.getUsers("Bearer $token")
    }

    suspend fun getUser(token: String, id: Int): UserModel {
        return userService.getUser("Bearer $token", id)
    }

    suspend fun updateUser(token: String, id: Int, profileRequest: ProfileRequest): UserModel {
        return userService.updateUser("Bearer $token", id, profileRequest)
    }

    suspend fun deleteUser(token: String, id: Int) {
        userService.deleteUser("Bearer $token", id)
    }

}