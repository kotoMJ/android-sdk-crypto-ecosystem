package cz.kotox.sdk.crypto.app.ui.screen.coins

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.kotox.crypto.sdk.coindata.CoinData
import cz.kotox.crypto.sdk.common.domain.model.coin.CurrencyId
import cz.kotox.crypto.sdk.common.fold
import cz.kotox.sdk.crypto.app.extension.stateInForUi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapNotNull
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CoinsViewModel(
    private val coinData: CoinData,
) : ViewModel() {

    val state: StateFlow<CoinsScreenState> = CoinsScreenPresenter(
        coinMarketsFlow = coinData.getCoinMarkets(CurrencyId("usd"))
            .mapNotNull { result ->
                result.fold(
                    { null },
                    { it },
                )
            },
    ).stateInForUi(
        scope = viewModelScope,
        initialValue = CoinsScreenState.Loading,
    )
}
