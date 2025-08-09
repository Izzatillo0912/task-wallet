package com.example.task.data.locale

import com.example.task.domain.cards.CardModel

object Cache {

    private val cards = arrayListOf(
        CardModel(id = 0, number = "", balance = 350000.0, method = "Cash", isSelected = true),
        CardModel(id = 1, number = "9860260110124096", balance = 480000.0, method = "Card", isSelected = false)
    )

    fun getCards() = cards

    fun addCard(card : CardModel) {
        cards.add(card)
    }
}