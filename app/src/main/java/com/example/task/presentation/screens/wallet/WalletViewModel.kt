package com.example.task.presentation.screens.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.data.locale.cards.CardsRepositoryImpl
import com.example.task.domain.cards.GetCardsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WalletViewModel : ViewModel() {

    private val getCardsUseCase = GetCardsUseCase(CardsRepositoryImpl())
    private val _state = MutableStateFlow(WalletScreenState())
    val state = _state.asStateFlow()

    fun handleIntent(intent: WalletScreenIntents) {
        when (intent) {
            is WalletScreenIntents.ShowPromoSheet -> {
                _state.update { it.copy(showPromoBottomSheet = intent.show) }
            }
            is WalletScreenIntents.SelectMethod -> {
                _state.update { state ->
                    if (intent.enable) {
                        state.copy(
                            cards = state.cards.map { card ->
                                card.copy(isSelected = card.id == intent.cardId)
                            },
                            enabledCardId = intent.cardId
                        )
                    } else {
                        state.copy(

                            cards = state.cards.map { card ->
                                if(intent.cardId != 0) {
                                    card.copy(isSelected = 0 == card.id)
                                }else {
                                    card.copy(isSelected = 1 == card.id)
                                }
                            },
                            enabledCardId = if(intent.cardId == 0)  1 else 0
                        )
                    }
                }
            }
            WalletScreenIntents.GetCards -> {
                viewModelScope.launch {
                    getCardsUseCase.invoke().collect { cards ->
                        _state.update { state ->
                            val selectedId = state.enabledCardId
                            val merged = cards.map { dbCard ->
                                dbCard.copy(isSelected = dbCard.id == selectedId)
                            }
                            state.copy(cards = merged)
                        }
                    }
                }
            }
        }
    }
}