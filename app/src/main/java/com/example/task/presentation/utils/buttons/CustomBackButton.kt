package com.example.task.presentation.utils.buttons

import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePaint
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomBackButton(
    modifier: Modifier = Modifier,
    shadowColor: Color = Color(0x14000000), // 8% black
    offsetX: Dp = 1.dp,
    offsetY: Dp = 1.dp,
    blurRadius: Dp = 16.dp,
    cornerRadius: Dp = 50.dp,
    spread: Dp = 0.dp,
    onClick: () -> Unit
) {
    val shadowColorArgb = shadowColor.toArgb()

    Box(
        modifier = modifier
            .drawBehind {
                val paint = Paint().apply {
                    this.color = shadowColorArgb
                    this.setShadowLayer(
                        blurRadius.toPx(),
                        offsetX.toPx(),
                        offsetY.toPx(),
                        shadowColorArgb
                    )
                }
                val spreadPx = spread.toPx()
                val rectLeft = 0f - spreadPx
                val rectTop = 0f - spreadPx
                val rectRight = size.width + spreadPx
                val rectBottom = size.height + spreadPx

                drawContext.canvas.save()
                drawContext.canvas.drawRoundRect(
                    rectLeft,
                    rectTop,
                    rectRight,
                    rectBottom,
                    cornerRadius.toPx(),
                    cornerRadius.toPx(),
                    paint = paint.asComposePaint()
                )
                drawContext.canvas.restore()
            }
            .clip(RoundedCornerShape(cornerRadius))
            .clickable { onClick() }
            .background(Color.White, shape = RoundedCornerShape(cornerRadius))
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = Color.Black,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}