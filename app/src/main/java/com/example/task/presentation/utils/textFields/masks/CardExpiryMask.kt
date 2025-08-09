package com.example.task.presentation.utils.textFields.masks

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class ExpiryDateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.filter { it.isDigit() }.take(4)
        val formatted = buildString {
            for (i in trimmed.indices) {
                append(trimmed[i])
                if (i == 1 && trimmed.length > 2) append("/") // faqat 2-raqamdan keyin qo'y
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 2 -> offset
                    offset in 3..4 -> offset + 1
                    else -> 5 // maksimal
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 2 -> offset
                    offset in 3..5 -> offset - 1
                    else -> 4
                }
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}
