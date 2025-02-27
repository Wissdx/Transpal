package fr.esgi.transpal.network.dto

data class LoginResponse(
    val token: String,
    val user: UserModel,
    val message: String? = null
)