package cz.kotox.sdk.crypto.app.ui.screen.coin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun CoinDetailScreen(
    viewModel: CoinDetailViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state is CoinDetailScreenState.Content) {
        CoinDetailContentScreen(
            coin = (state as CoinDetailScreenState.Content).coin,
            {},
        )
    }
}
