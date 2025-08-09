package com.example.task.presentation.utils

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.SwipeableState
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun CustomSwitch(
    onCheckedChange: (Boolean) -> Unit,
    checked: Boolean,
    width: Dp = 48.dp,
    height: Dp = 27.dp,
    checkedColor: Color = Color.Black,
    uncheckedColor: Color = Color(0xFFECEDF1),
    thumbColor: Color = Color.White
) {
    val swappableState = rememberSwipeableState(if (checked) 1 else 0)
    val sizePx = with(LocalDensity.current) { (width - height).toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1)
    val scope = rememberCoroutineScope()

    var userTriggered by remember { mutableStateOf(false) }

    LaunchedEffect(checked) {
        Log.e("SwitchChanged", "Checked: $checked")
        swappableState.animateTo(if (checked) 1 else 0)
    }

    LaunchedEffect(Unit) {
        snapshotFlow { swappableState.currentValue }
            .collectLatest { value ->
                swappableState.awaitAnimationEnd() // Bu yerda ishlaydi endi
                if (userTriggered) {
                    onCheckedChange(value == 1)
                    userTriggered = false
                }
            }
    }

    Row(
        modifier = Modifier
            .height(height)
            .clickable(indication = null, interactionSource = null) {
                val newState = if (swappableState.currentValue == 0) 1 else 0
                scope.launch {
                    swappableState.animateTo(newState)
                }
                onCheckedChange(newState == 1)
            }
            .focusable(enabled = false, interactionSource = null)
            .width(width)
            .clip(RoundedCornerShape(height))
            .background(color = if (checked) checkedColor else uncheckedColor),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .offset { IntOffset(swappableState.offset.value.roundToInt(), 0) }
                .swipeable(
                    state = swappableState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    orientation = Orientation.Horizontal
                )
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent()
                            userTriggered = true
                        }
                    }
                }
                .size(height - 1.dp)
                .padding(all = 2.dp)
                .clip(RoundedCornerShape(50))
                .background(thumbColor)
        )
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
suspend fun SwipeableState<Int>.awaitAnimationEnd() {
    snapshotFlow { isAnimationRunning }
        .filter { !it }
        .first()
}