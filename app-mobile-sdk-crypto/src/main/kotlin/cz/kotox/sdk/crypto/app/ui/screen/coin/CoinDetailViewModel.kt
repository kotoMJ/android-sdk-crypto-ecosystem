package cz.kotox.sdk.crypto.app.ui.screen.coin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.kotox.crypto.sdk.coindata.CoinData
import cz.kotox.crypto.sdk.common.domain.model.coin.CoinMarketId
import cz.kotox.crypto.sdk.common.fold
import cz.kotox.sdk.crypto.app.extension.stateInForUi
import cz.kotox.sdk.crypto.app.ui.CoinDetailScreenRoute
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapNotNull
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class CoinDetailViewModel(
    val navKey: CoinDetailScreenRoute,
    private val coinData: CoinData,
) : ViewModel() {

    val id = navKey.id

    val state: StateFlow<CoinDetailScreenState> = CoinDetailScreenPresenter(
        coinFlow = coinData.getCoinDetail(coinMarketId = CoinMarketId(id))
            .mapNotNull { result ->
                result.fold(
                    { null },
                    { it },
                )
            },
    ).stateInForUi(
        scope = viewModelScope,
        initialValue = CoinDetailScreenState.Loading,
    )
}
