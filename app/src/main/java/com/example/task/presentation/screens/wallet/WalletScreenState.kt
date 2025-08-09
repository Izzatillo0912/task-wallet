package com.example.task.presentation.screens.wallet

import com.example.task.domain.cards.CardModel

data class WalletScreenState(
    var cashEnabled: Boolean = true,
    var enabledCardId: Int? = null,
    val showPromoBottomSheet: Boolean = false,
    val cards: List<CardModel> = emptyList()
)