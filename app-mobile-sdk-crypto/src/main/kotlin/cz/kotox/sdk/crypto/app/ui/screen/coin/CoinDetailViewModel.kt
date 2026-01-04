package cz.kotox.sdk.crypto.app.ui.screen.coin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.kotox.crypto.sdk.coindata.CoinData
import cz.kotox.crypto.sdk.coindata.domain.model.CoinDetail
import cz.kotox.crypto.sdk.common.domain.model.coin.CoinMarketId
import cz.kotox.crypto.sdk.common.fold
import cz.kotox.sdk.crypto.app.extension.stateInForUi
import cz.kotox.sdk.crypto.app.ui.CoinDetailScreenRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class CoinDetailViewModel(
    val navKey: CoinDetailScreenRoute,
    private val coinData: CoinData,
) : ViewModel() {

    val id = navKey.id

    private val coinFlow: MutableStateFlow<CoinDetail?> = MutableStateFlow(null)

    val state: StateFlow<CoinDetailScreenState> = CoinDetailScreenPresenter(
        coinFlow = coinFlow,
    ).onStart {
        initData()
    }.stateInForUi(
        scope = viewModelScope,
        initialValue = CoinDetailScreenState.Loading,
    )

    private fun initData() {
        viewModelScope.launch {
            coinData.getCoinDetail(coinMarketId = CoinMarketId(id)).fold(
                {},
                { coin ->
                    coinFlow.update { coin } // coinDetailBitcoin
                },
            )
        }
    }
}
