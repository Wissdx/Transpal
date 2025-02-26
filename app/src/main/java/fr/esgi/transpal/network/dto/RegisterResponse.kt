package fr.esgi.transpal.network.dto

data class RegisterResponse(
    val id: Int,
    val email: String,
    val password: String,
    val name: String,
    val updatedAt: String,
    val createdAt: String
)

