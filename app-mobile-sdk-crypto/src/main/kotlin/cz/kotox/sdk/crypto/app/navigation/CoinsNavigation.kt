package cz.kotox.sdk.crypto.app.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import cz.kotox.sdk.crypto.app.ui.screen.coins.CoinsScreen

const val ROUTE_COINS = "coins"

fun NavGraphBuilder.coinsScreen() {
    composable(route = ROUTE_COINS) {
        CoinsScreen()
    }
}
