package fr.esgi.transpal.network.dto

data class RegisterRequest(
    val email: String,
    val name: String,
    val password: String
)