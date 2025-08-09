package com.example.task.presentation.screens.addCard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.data.locale.cards.CardsRepositoryImpl
import com.example.task.domain.cards.AddCardUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddCardViewModel : ViewModel() {

    private val useCase = AddCardUseCase(CardsRepositoryImpl())
    private val _state = MutableStateFlow(AddCardState())
    val state = _state.asStateFlow()

    private companion object {
        const val CARD_NUMBER_LENGTH = 16
        const val EXPIRY_DATE_LENGTH = 4
    }

    fun handleIntent(intent: AddCardIntents) {
        when (intent) {
            is AddCardIntents.KeyboardInput -> handleKeyboardInput(intent.key)
            is AddCardIntents.AddCard -> handleAddCard(intent.number)
            AddCardIntents.SelectCardNumber -> _state.update {
                it.copy(cardNumberIsSelected = true, expireDateIsSelected = false)
            }
            AddCardIntents.SelectExpiryDate -> _state.update {
                it.copy(cardNumberIsSelected = false, expireDateIsSelected = true)
            }
        }
    }

    private fun handleAddCard(number: String) {
        viewModelScope.launch {
            useCase.invoke(number).collect { unit ->
                _state.update {
                    it.copy(addedNewCard = unit)
                }
            }
        }
    }

    private fun handleKeyboardInput(key: String) {
        _state.update { state ->
            var cardNumber = state.cardNumber
            var expiryDate = state.expiryDate
            var cardSelected = state.cardNumberIsSelected
            var expirySelected = state.expireDateIsSelected

            if (key.isEmpty()) { // Backspace
                if (cardSelected && cardNumber.isNotEmpty()) {
                    cardNumber = cardNumber.dropLast(1)
                } else if (expirySelected && expiryDate.isNotEmpty()) {
                    expiryDate = expiryDate.dropLast(1)
                }
            } else {
                when {
                    cardSelected -> {
                        if (cardNumber.length < CARD_NUMBER_LENGTH) {
                            cardNumber += key
                        } else if (expiryDate.length < EXPIRY_DATE_LENGTH) {
                            cardSelected = false
                            expirySelected = true
                            expiryDate += key
                        } else {
                            cardSelected = false
                        }
                    }

                    expirySelected -> {
                        if (expiryDate.length < EXPIRY_DATE_LENGTH) {
                            expiryDate += key
                        } else if (cardNumber.length < CARD_NUMBER_LENGTH) {
                            cardSelected = true
                            expirySelected = false
                            cardNumber += key
                        } else {
                            expirySelected = false
                        }
                    }

                    else -> {
                        if (cardNumber.length < CARD_NUMBER_LENGTH) {
                            cardSelected = true
                            cardNumber += key
                        } else if (expiryDate.length < EXPIRY_DATE_LENGTH) {
                            expirySelected = true
                            expiryDate += key
                        }
                    }
                }
            }

            state.copy(
                cardNumber = cardNumber,
                expiryDate = expiryDate,
                cardNumberIsSelected = cardSelected,
                expireDateIsSelected = expirySelected,
                isSaveEnabled = cardNumber.length == CARD_NUMBER_LENGTH &&
                        expiryDate.length == EXPIRY_DATE_LENGTH
            )
        }
    }
}
