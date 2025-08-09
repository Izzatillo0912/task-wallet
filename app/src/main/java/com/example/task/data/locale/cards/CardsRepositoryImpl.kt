package com.example.task.data.locale.cards

import com.example.task.data.locale.Cache
import com.example.task.domain.cards.CardModel
import com.example.task.domain.cards.CardsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CardsRepositoryImpl : CardsRepository {

    override suspend fun getCards(): Flow<List<CardModel>> {
        return flow {
            emit(Cache.getCards())
        }
    }

    override suspend fun addCard(number : String) : Flow<Unit>{
        return flow {
            emit(
                Cache.addCard(
                    CardModel(
                        id = Cache.getCards().size,
                        number = number,
                        balance = (100_000..850_000).random().toDouble()
                    )
                )
            )
        }
    }
}