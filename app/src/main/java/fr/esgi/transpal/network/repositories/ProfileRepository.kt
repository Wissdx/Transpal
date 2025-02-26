package fr.esgi.transpal.network.repositories

import fr.esgi.transpal.network.dto.ProfileRequest
import fr.esgi.transpal.network.dto.ProfileResponse

class ProfileRepository {

    suspend fun getProfile(): ProfileResponse {
        // Simulate network call
        return ProfileResponse("John Doe", "john.doe@example.com")
    }

    suspend fun updateProfile(profileRequest: ProfileRequest): ProfileResponse {
        // Simulate network call
        return ProfileResponse(profileRequest.name, profileRequest.email)
    }
}