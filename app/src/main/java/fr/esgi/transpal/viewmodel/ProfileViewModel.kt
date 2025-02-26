package fr.esgi.transpal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.esgi.transpal.network.dto.ProfileRequest
import fr.esgi.transpal.network.dto.ProfileResponse
import fr.esgi.transpal.network.repositories.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {

    private val _profile = MutableLiveData<ProfileResponse>()
    val profile: LiveData<ProfileResponse> get() = _profile

    fun loadProfile() {
        viewModelScope.launch {
            val profile = profileRepository.getProfile()
            _profile.value = profile
        }
    }

    fun updateProfile(profileRequest: ProfileRequest) {
        viewModelScope.launch {
            val updatedProfile = profileRepository.updateProfile(profileRequest)
            _profile.value = updatedProfile
        }
    }
}