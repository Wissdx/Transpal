package fr.esgi.transpal.network.dto

data class LoginResponse(
    val token: String,
    val user: User,
    val message: String? = null
)

data class User(
    val id: Int,
    val email: String,
    val name: String,
    val createdAt: String,
    val updatedAt: String
)