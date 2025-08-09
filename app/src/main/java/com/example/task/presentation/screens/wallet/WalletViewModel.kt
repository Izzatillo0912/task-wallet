package com.example.task.presentation.screens.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.data.locale.cards.CardsRepositoryImpl
import com.example.task.domain.cards.CardsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WalletViewModel : ViewModel() {

    private val cardsUseCase = CardsUseCase(CardsRepositoryImpl())
    private val _state = MutableStateFlow(WalletScreenState())
    val state = _state.asStateFlow()

    fun handleIntent(intent: WalletScreenIntents) {
        when (intent) {
            is WalletScreenIntents.ShowPromoSheet -> {
                _state.update { it.copy(showPromoBottomSheet = intent.show) }
            }
            is WalletScreenIntents.CashMethodEnable -> {
                _state.update { state ->
                    if (intent.enable) {
                        state.copy(
                            cashEnabled = true,
                            cards = state.cards.map { it.copy(isSelected = false) }
                        )
                    } else {
                        if (state.cards.isNotEmpty()) {
                            state.copy(
                                cashEnabled = false,
                                cards = state.cards.mapIndexed { index, card ->
                                    card.copy(isSelected = index == 0)
                                }
                            )
                        } else {
                            state.copy(cashEnabled = false)
                        }
                    }
                }
            }

            is WalletScreenIntents.CardMethodEnable -> {
                _state.update { state ->
                    if (intent.enable) {
                        state.copy(
                            cashEnabled = false,
                            cards = state.cards.map { card ->
                                card.copy(isSelected = card.id == intent.cardId)
                            },
                            enabledCardId = intent.cardId
                        )
                    } else {
                        state.copy(
                            cashEnabled = true,
                            cards = state.cards.map { it.copy(isSelected = false) },
                            enabledCardId = null
                        )
                    }
                }
            }
            WalletScreenIntents.GetCards -> {
                viewModelScope.launch {
                    cardsUseCase.getCards().collect { cards ->
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