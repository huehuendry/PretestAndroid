package com.hendry.androidpretest.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hendry.androidpretest.model.Transaction
import com.hendry.androidpretest.model.TransactionType

@Composable
fun AppScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {
            HomeScreen(
                onNavigateToTransfer = { type ->
                    navController.navigate("gather/${type.name}")
                }
            )
        }


        composable("gather/{type}") { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type")

            GatherScreen(
                type = TransactionType.valueOf(type ?: "TRANSFER"),
                onNavigateToReceipt = { transaction ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("transaction", transaction)

                    navController.navigate("receipt")
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("receipt") {
            val transaction =
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<Transaction>("transaction")

            transaction?.let {
                ReceiptScreen(
                    transaction = it,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}