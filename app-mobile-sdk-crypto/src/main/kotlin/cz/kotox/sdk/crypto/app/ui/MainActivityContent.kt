package cz.kotox.sdk.crypto.app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import cz.kotox.sdk.crypto.app.navigation.BottomSheetSceneStrategy
import cz.kotox.sdk.crypto.app.navigation.CompositeSceneStrategy
import cz.kotox.sdk.crypto.app.ui.component.Screen
import cz.kotox.sdk.crypto.app.ui.screen.coins.CoinsScreen
import cz.kotox.sdk.crypto.app.ui.screen.currency.CurrencyScreen
import kotlinx.serialization.Serializable

@Serializable
private data object CoinsScreenRoute : NavKey

@Serializable
private data object CurrencyScreenRoute : NavKey

// @Serializable
// private data class CoinsScreenDetailRoute(val id: String) : NavKey

@Composable
fun MainActivityContent(
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack(CoinsScreenRoute)
    val bottomSheetStrategy = remember { BottomSheetSceneStrategy<NavKey>() }
    val dialogStrategy = remember { DialogSceneStrategy<NavKey>() }

    val appSceneStrategy = remember {
        CompositeSceneStrategy(
            listOf(
                bottomSheetStrategy, // Priority 1
                dialogStrategy, // Priority 2
            ),
        )
    }

    Screen(
        modifier = modifier
            .fillMaxSize()
            .testTag("main_activity"),
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            sceneStrategy = appSceneStrategy,
            entryProvider = entryProvider {
                entry<CoinsScreenRoute> {
                    CoinsScreen(
                        onItemClick = {
                            backStack.add(CurrencyScreenRoute)
                        },
                    )
                }

//                    is CoinsScreenDetailRoute -> NavEntry(key) {
//                        //
//                    }

                entry<CurrencyScreenRoute>(
                    // metadata = DialogSceneStrategy.dialog()
                    metadata = BottomSheetSceneStrategy.bottomSheet(),
                ) {
                    CurrencyScreen()
                }
            },
        )
    }
}
