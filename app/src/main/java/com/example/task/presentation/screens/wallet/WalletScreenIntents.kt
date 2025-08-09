package com.example.task.presentation.screens.wallet


sealed interface WalletScreenIntents {
    data class ShowPromoSheet(val show: Boolean) : WalletScreenIntents
    data class SelectMethod(val cardId: Int, val enable: Boolean) : WalletScreenIntents
    data object GetCards : WalletScreenIntents
}