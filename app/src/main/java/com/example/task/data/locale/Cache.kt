package com.example.task.data.locale

import com.example.task.domain.cards.CardModel

object Cache {

    private val cards = arrayListOf(
        CardModel(id = 0, number = "9860260110124096", balance = 350000.0, isSelected = false)
    )

    fun getCards() = cards

    fun addCard(card : CardModel) {
        cards.add(card)
    }
}