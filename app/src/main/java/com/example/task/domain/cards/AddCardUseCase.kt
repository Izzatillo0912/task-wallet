package com.example.task.domain.cards

import kotlinx.coroutines.flow.Flow

class AddCardUseCase(private val repository: CardsRepository) {
    suspend operator fun invoke(number: String): Flow<Unit> {
        return repository.addCard(number)
    }
}