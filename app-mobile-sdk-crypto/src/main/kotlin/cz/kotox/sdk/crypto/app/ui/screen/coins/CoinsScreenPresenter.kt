package cz.kotox.sdk.crypto.app.ui.screen.coins

import cz.kotox.crypto.sdk.coindata.domain.model.CoinMarket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Suppress("FunctionName")
internal fun CoinsScreenPresenter(
    coinMarketsFlow: Flow<List<CoinMarket>?>,
): Flow<CoinsScreenState> = coinMarketsFlow.map { coinMarkets ->
    if (coinMarkets == null) {
        CoinsScreenState.Loading
    } else {
        CoinsScreenState.Content(
            coinMarkets = coinMarkets,
        )
    }
}
