package com.example.task.presentation.screens.addCard

sealed interface AddCardIntents {
    data class KeyboardInput(val key: String) : AddCardIntents
    data object SelectCardNumber : AddCardIntents
    data object SelectExpiryDate : AddCardIntents
    data class AddCard(val number: String) : AddCardIntents
}