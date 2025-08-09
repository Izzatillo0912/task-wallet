package com.example.task.presentation.utils

import java.text.NumberFormat
import java.util.Locale


fun Double.moneyFormat(): String {
    val formatter = NumberFormat.getNumberInstance(Locale.US)
    formatter.minimumFractionDigits = 2
    formatter.maximumFractionDigits = 2

    return formatter.format(this)
}

