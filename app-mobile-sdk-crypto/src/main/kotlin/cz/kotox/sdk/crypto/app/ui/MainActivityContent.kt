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
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import cz.kotox.sdk.crypto.app.navigation.BottomSheetSceneStrategy
import cz.kotox.sdk.crypto.app.navigation.CompositeSceneStrategy
import cz.kotox.sdk.crypto.app.ui.component.Screen
import cz.kotox.sdk.crypto.app.ui.screen.coin.CoinDetailScreen
import cz.kotox.sdk.crypto.app.ui.screen.coin.CoinDetailViewModel
import cz.kotox.sdk.crypto.app.ui.screen.coins.CoinsScreen
import cz.kotox.sdk.crypto.app.ui.screen.currency.CurrencyScreen
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.time.Duration

@Serializable
private data object CoinsScreenRoute : NavKey

@Serializable
private data object CurrencyScreenRoute : NavKey

@Serializable
internal data class CoinDetailScreenRoute(val id: String) : NavKey

@Suppress("LongMethod")
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
        } else {
            showFab = false
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
            entryDecorators = listOf(
                // In order to add the `ViewModelStoreNavEntryDecorator` (see comment below for why)
                // we also need to add the default `NavEntryDecorator`s as well. These provide
                // extra information to the entry's content to enable it to display correctly
                // and save its state.
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                entry<CoinsScreenRoute> {
                    CoinsScreen(
                        onItemClick = { id ->
                            backStack.add(CoinDetailScreenRoute(id))
                        },
                        contentAvailable = {
                            mainContentAvailable = it
                        },
                    )
                }

                entry<CoinDetailScreenRoute> { key ->

                    val viewModel = koinViewModel<CoinDetailViewModel> {
                        parametersOf(key)
                    }
                    CoinDetailScreen(viewModel = viewModel)
                }

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
