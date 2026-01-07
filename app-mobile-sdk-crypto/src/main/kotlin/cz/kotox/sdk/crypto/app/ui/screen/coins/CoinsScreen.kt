package cz.kotox.sdk.crypto.app.ui.screen.coins

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun CoinsScreen(
    onItemClick: (String) -> Unit,
    contentAvailable: (Boolean) -> Unit,
    viewModel: CoinsViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        contentAvailable(state is CoinsScreenState.Content)
    }

    when (val localState = state) {
        is CoinsScreenState.Content -> {
            CoinsContentScreen(
                state = localState,
                onItemClick = onItemClick,
                contentPadding = contentPadding,
            )
        }

        CoinsScreenState.Loading -> {}
    }
}
