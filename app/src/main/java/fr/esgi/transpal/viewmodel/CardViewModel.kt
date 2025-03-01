package fr.esgi.transpal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.esgi.transpal.network.dto.AddCardRequest
import fr.esgi.transpal.network.dto.CardResponse
import fr.esgi.transpal.network.repositories.CardRepository
import kotlinx.coroutines.launch

class CardViewModel(private val cardRepository: CardRepository) : ViewModel() {

    private val _cards = MutableLiveData<List<CardResponse>>()
    val cards: LiveData<List<CardResponse>> get() = _cards

    fun addCard(token: String, userId: Int, addCardRequest: AddCardRequest, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                cardRepository.addCard(token, userId, addCardRequest)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Erreur inconnue")
            }
        }
    }

    fun getCards(token: String, userId: Int) {
        viewModelScope.launch {
            try {
                val cards = cardRepository.getCards(token, userId)
                _cards.postValue(cards)
            } catch (e: Exception) {
                _cards.postValue(emptyList())
            }
        }
    }
}