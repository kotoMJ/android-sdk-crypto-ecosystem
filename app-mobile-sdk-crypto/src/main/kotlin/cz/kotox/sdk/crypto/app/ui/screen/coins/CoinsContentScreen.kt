package cz.kotox.sdk.crypto.app.ui.screen.coins

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cz.kotox.sdk.crypto.app.ui.mock.coins.mockCoinMarkets

@Composable
fun CoinsContentScreen(
    state: CoinsScreenState.Content,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LazyColumn {
                items(
                    items = state.coinMarkets,
                    key = { it.id },
                ) { market ->
                    CoinListItem(market = market)
                }
            }
        }
    }
}

/**
 * Compose @Preview for the CoinsContentScreen.
 *
 * We wrap it in MaterialTheme because the real composable
 * depends on it for typography and colors.
 */
@Preview(showBackground = true)
@Composable
fun CoinsContentScreenPreview() {
    // Assuming you have a MaterialTheme defined in your app,
    // otherwise, replace with your app's theme.
    MaterialTheme {
        CoinsContentScreen(
            state = CoinsScreenState.Content(
                coinMarkets = mockCoinMarkets,
            ),
        )
    }
}
