package com.example.task.presentation.utils.textFields.masks

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CreditCardVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.filter { it.isDigit() }.take(16)
        val formatted = buildString {
            for (i in trimmed.indices) {
                append(trimmed[i])
                if ((i + 1) % 4 == 0 && i != trimmed.length - 1) append("  ") // 2 ta space
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val clamped = offset.coerceAtMost(trimmed.length)
                val spaceCount = (0 until clamped).count { (it + 1) % 4 == 0 && it != trimmed.length - 1 }
                return clamped + (spaceCount * 2)
            }

            override fun transformedToOriginal(offset: Int): Int {
                var spaceCount = 0
                var i = 0
                while (i < offset) {
                    val originalIndex = i - spaceCount
                    if ((originalIndex + 1) % 4 == 0 && originalIndex != trimmed.length - 1) {
                        spaceCount += 2
                        i += 2
                    } else {
                        i++
                    }
                }
                return (offset - spaceCount).coerceAtMost(trimmed.length)
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}
