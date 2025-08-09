package com.example.task.domain.cards

import kotlinx.coroutines.flow.Flow

interface CardsRepository {

    suspend fun getCards() : Flow<List<CardModel>>

    suspend fun addCard(number: String) : Flow<Unit>
}