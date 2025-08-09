package com.example.task.presentation.screens.wallet

import com.example.task.domain.cards.CardModel

data class WalletScreenState(
    var enabledCardId: Int = 0,
    val showPromoBottomSheet: Boolean = false,
    val cards: List<CardModel> = emptyList()
)