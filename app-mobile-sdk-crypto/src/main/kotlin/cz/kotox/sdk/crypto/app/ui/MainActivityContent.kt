package cz.kotox.sdk.crypto.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import kotlinx.serialization.Serializable
import java.time.Duration

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
    val scope = rememberCoroutineScope()

    val appSceneStrategy = remember {
        CompositeSceneStrategy(
            listOf(
                bottomSheetStrategy, // Priority 1
                dialogStrategy, // Priority 2
            ),
        )
    }

    var mainContentAvailable: Boolean by remember {
        mutableStateOf(false)
    }

    var showFab by remember { mutableStateOf(false) }
    val currentRoute = backStack.lastOrNull()

    LaunchedEffect(currentRoute, mainContentAvailable) {
        if (currentRoute is CoinsScreenRoute && mainContentAvailable) {
            showFab = true
        }
    }

    val fab: @Composable () -> Unit = {
        AnimatedVisibility(
            visible = showFab,
            enter = fadeIn(animationSpec = tween(durationMillis = 200)),
            exit = fadeOut(animationSpec = tween(durationMillis = 200)),
        ) {
            val rotation by this.transition.animateFloat(
                transitionSpec = { tween(durationMillis = 200) },
                label = "FabRotation",
            ) { state ->
                when (state) {
                    EnterExitState.PreEnter -> -90f
                    EnterExitState.Visible -> 0f
                    EnterExitState.PostExit -> 90f
                }
            }

            FloatingActionButton(
                onClick = {
                    scope.launch {
                        showFab = false
                        delay(Duration.ofMillis(150))
                        backStack.add(CurrencyScreenRoute)
                    }
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.graphicsLayer {
                    rotationZ = rotation
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.CurrencyExchange,
                    contentDescription = "Change Currency",
                )
            }
        }
    }

    Screen(
        modifier = modifier
            .fillMaxSize()
            .testTag("main_activity"),
        fab = fab,
    ) { paddingValues ->
        NavDisplay(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            sceneStrategy = appSceneStrategy,
            entryProvider = entryProvider {
                entry<CoinsScreenRoute> {
                    CoinsScreen(
                        onItemClick = {
                            // TODO: Add your detail route logic
                            // e.g., backStack.add(CoinsScreenDetailRoute(it.id))
                        },
                        contentAvailable = {
                            mainContentAvailable = it
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
