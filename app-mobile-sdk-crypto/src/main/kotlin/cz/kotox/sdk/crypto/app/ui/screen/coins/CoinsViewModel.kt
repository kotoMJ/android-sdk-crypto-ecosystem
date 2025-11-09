package cz.kotox.sdk.crypto.app.ui.screen.coins

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.kotox.crypto.sdk.coindata.CoinData
import cz.kotox.crypto.sdk.coindata.domain.model.CoinMarket
import cz.kotox.crypto.sdk.common.fold
import cz.kotox.sdk.crypto.app.extension.stateInForUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CoinsViewModel(
    private val coinData: CoinData,
) : ViewModel() {

    private val coinMarketsFlow: MutableStateFlow<List<CoinMarket>?> = MutableStateFlow(null)

    val state: StateFlow<CoinsScreenState> = CoinsScreenPresenter(
        coinMarketsFlow = coinMarketsFlow,
    ).onStart {
        initData()
    }.stateInForUi(
        scope = viewModelScope,
        initialValue = CoinsScreenState.Loading,
    )

    private fun initData() {
        viewModelScope.launch {
            coinData.getCoinMarkets("usd").fold(
                {},
                { markets ->
                    coinMarketsFlow.update { markets }
                },
            )
        }
    }
}
