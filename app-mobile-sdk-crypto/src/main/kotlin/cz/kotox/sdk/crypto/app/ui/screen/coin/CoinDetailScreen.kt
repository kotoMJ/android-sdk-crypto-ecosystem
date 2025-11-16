package cz.kotox.sdk.crypto.app.ui.screen.coin

import androidx.compose.runtime.Composable

@Composable
internal fun CoinDetailScreen(
    viewModel: CoinDetailViewModel,
) {
    // val state by viewModel.state.collectAsStateWithLifecycle()

    CoinDetailContentScreen(
        id = viewModel.id,
    )
}
