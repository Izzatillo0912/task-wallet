package com.example.task.domain.cards

import kotlinx.coroutines.flow.Flow

class CardsUseCase(private val repository: CardsRepository) {

    suspend fun getCards() : Flow<List<CardModel>> {
        return repository.getCards()
    }

    suspend fun addCard(number : String) : Flow<Unit> {
        return repository.addCard(number)
    }
}