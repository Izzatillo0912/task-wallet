package com.example.task.presentation.bottomSheets.addPromoCode

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddPromoCodeViewModel : ViewModel() {

    private val _state = MutableStateFlow(AddPromoCodeState())
    val state = _state.asStateFlow()

    fun handleIntent(intent: AddPromoCodeIntent) {
        when (intent) {
            is AddPromoCodeIntent.PromoCodeChanged -> {
                _state.update {
                    it.copy(
                        promoCode = intent.value,
                        isSaveEnabled = intent.value.isNotBlank()
                    )
                }
            }
            AddPromoCodeIntent.ClearState -> {
                _state.value = AddPromoCodeState()
            }
            AddPromoCodeIntent.SaveClicked -> {

            }
        }
    }
}
