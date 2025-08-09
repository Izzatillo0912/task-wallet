package com.example.task.domain.cards

data class CardModel(
    val id : Int,
    val number : String,
    val balance : Double,
    val method : String,
    var isSelected : Boolean = false
)
