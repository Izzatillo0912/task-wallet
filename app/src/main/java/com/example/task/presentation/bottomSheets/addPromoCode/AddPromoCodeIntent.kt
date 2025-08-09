package com.example.task.presentation.bottomSheets.addPromoCode

sealed interface AddPromoCodeIntent {
    data object ClearState : AddPromoCodeIntent
    data class PromoCodeChanged(val value: String) : AddPromoCodeIntent
    data object SaveClicked : AddPromoCodeIntent
}
