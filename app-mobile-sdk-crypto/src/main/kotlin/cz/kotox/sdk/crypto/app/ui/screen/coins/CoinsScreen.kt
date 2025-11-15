package cz.kotox.sdk.crypto.app.ui.screen.coins

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun CoinsScreen(
    onItemClick: () -> Unit,
    viewModel: CoinsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val localState = state) {
        is CoinsScreenState.Content -> {
            CoinsContentScreen(
                state = localState,
                onItemClick = onItemClick,
            )
        }

        CoinsScreenState.Loading -> {}
    }
}
