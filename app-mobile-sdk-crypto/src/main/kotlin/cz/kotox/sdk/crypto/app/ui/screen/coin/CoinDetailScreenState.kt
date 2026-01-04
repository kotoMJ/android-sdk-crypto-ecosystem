package cz.kotox.sdk.crypto.app.ui.screen.coin

import androidx.compose.runtime.Immutable
import cz.kotox.crypto.sdk.coindata.domain.model.CoinDetail

@Immutable
sealed interface CoinDetailScreenState {

    data object Loading : CoinDetailScreenState

    data class Content(
        val coin: CoinDetail,
    ) : CoinDetailScreenState
}
