package com.example.task.presentation.screens.addCard

data class AddCardState(
    val cardNumber: String = "",
    val expiryDate: String = "",
    val cardNumberIsSelected: Boolean = false,
    val expireDateIsSelected: Boolean = false,
    val isSaveEnabled: Boolean = false,
    val addedNewCard: Unit = Unit
)