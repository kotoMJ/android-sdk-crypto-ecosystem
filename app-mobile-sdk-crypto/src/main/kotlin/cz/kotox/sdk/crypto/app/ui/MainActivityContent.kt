package cz.kotox.sdk.crypto.app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import cz.kotox.sdk.crypto.app.ui.component.Screen
import cz.kotox.sdk.crypto.app.ui.screen.coins.CoinsScreen
import kotlinx.serialization.Serializable

@Serializable
private data object CoinsScreenRoute : NavKey

// @Serializable
// private data class CoinsScreenDetailRoute(val id: String) : NavKey

@Composable
fun MainActivityContent(
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack(CoinsScreenRoute)

    Screen(
        modifier = modifier
            .fillMaxSize()
            .testTag("main_activity"),
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = { key ->
                when (key) {
                    is CoinsScreenRoute -> NavEntry(key) {
                        CoinsScreen()
                    }

//                    is CoinsScreenDetailRoute -> NavEntry(key) {
//                        //
//                    }

                    else -> {
                        error("Unknown route: $key")
                    }
                }
            },
        )
    }
}
