package com.example.task.presentation.utils.buttons
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.task.presentation.utils.theme.ButtonBlack
import com.example.task.presentation.utils.theme.FigTree

@Composable
fun SaveButton(
    modifier: Modifier = Modifier,
    enabled : Boolean,
    onClick : ()-> Unit
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonColors(
            containerColor = ButtonBlack,
            contentColor = Color.White,
            disabledContainerColor = Color(0xFFD9DBE2),
            disabledContentColor = Color(0xFFA1A5B7)
        )
    ) {
        Text(
            modifier = Modifier.padding(top = 7.dp, bottom = 7.dp),
            text = "Save",
            fontFamily = FigTree,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}