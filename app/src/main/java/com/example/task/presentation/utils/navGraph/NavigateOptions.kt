package com.example.task.presentation.utils.navGraph

import androidx.navigation.NavController

private var lastBackPressTime = 0L
private const val BACK_PRESS_DEBOUNCE = 300L // ms

fun NavController.navigateSafely(route: String) {
    val currentRoute = this.currentBackStackEntry?.destination?.route
    if (currentRoute != route) {
        this.navigate(route) {
            launchSingleTop = true
        }
    }
}

fun NavController.popBackStackSafely(): Boolean {
    val now = System.currentTimeMillis()
    return if (now - lastBackPressTime > BACK_PRESS_DEBOUNCE) {
        lastBackPressTime = now
        this.popBackStack()
    } else {
        false
    }
}