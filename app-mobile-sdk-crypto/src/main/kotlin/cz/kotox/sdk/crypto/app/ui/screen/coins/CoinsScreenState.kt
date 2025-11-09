package cz.kotox.sdk.crypto.app.ui.screen.coins

import androidx.compose.runtime.Immutable
import cz.kotox.crypto.sdk.coindata.domain.model.CoinMarket

@Immutable
sealed interface CoinsScreenState {

    data object Loading : CoinsScreenState

    data class Content(
        val coinMarkets: List<CoinMarket>,
    ) : CoinsScreenState
}
