package com.example.task.presentation.screens.wallet

import com.example.task.domain.cards.CardModel

sealed class WalletScreenIntents {
    data class ShowPromoSheet(val show: Boolean) : WalletScreenIntents()
    data class CashMethodEnable(val enable: Boolean) : WalletScreenIntents()
    data class CardMethodEnable(val cardId: Int, val enable: Boolean) : WalletScreenIntents()
    data object GetCards : WalletScreenIntents()
}