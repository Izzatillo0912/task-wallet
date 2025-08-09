package com.example.task.presentation.utils.theme

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun SetSystemBarsColor(
    statusBarColor: Color = Color.White,
    navigationBarColor: Color = Color.White,
    darkIcons: Boolean = true
) {
    val context = LocalContext.current
    val activity = context as? Activity ?: return
    val window = activity.window

    SideEffect {
        // Status bar and navigation bar colors
        window.statusBarColor = statusBarColor.toArgb()
        window.navigationBarColor = navigationBarColor.toArgb()

        // System bar icon colors
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = darkIcons
        insetsController.isAppearanceLightNavigationBars = darkIcons

        // Enable drawing behind system bars
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}
