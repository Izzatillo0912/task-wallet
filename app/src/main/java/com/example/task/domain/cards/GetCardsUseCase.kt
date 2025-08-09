package com.example.task.domain.cards

import kotlinx.coroutines.flow.Flow

class GetCardsUseCase(private val repository: CardsRepository) {
    suspend operator fun invoke(): Flow<List<CardModel>> {
        return repository.getCards()
    }
}