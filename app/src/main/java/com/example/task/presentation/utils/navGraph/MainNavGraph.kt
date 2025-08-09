package com.example.task.presentation.utils.navGraph

import com.example.task.presentation.screens.wallet.WalletScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.task.presentation.screens.addCard.AddCardScreen


@Composable
fun MainNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "wallet",
    ) {
        navComposable("wallet") {
            WalletScreen(navController = navController)
        }
        navComposable("addCard") {
            AddCardScreen(navController = navController)
        }
    }
}

