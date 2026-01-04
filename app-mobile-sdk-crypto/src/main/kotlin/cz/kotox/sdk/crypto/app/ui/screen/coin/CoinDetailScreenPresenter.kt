package cz.kotox.sdk.crypto.app.ui.screen.coin

import cz.kotox.crypto.sdk.coindata.domain.model.CoinDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Suppress("FunctionName")
internal fun CoinDetailScreenPresenter(
    coinFlow: Flow<CoinDetail?>,
): Flow<CoinDetailScreenState> = coinFlow.map { coin ->
    if (coin == null) {
        CoinDetailScreenState.Loading
    } else {
        CoinDetailScreenState.Content(
            coin = coin,
        )
    }
}
