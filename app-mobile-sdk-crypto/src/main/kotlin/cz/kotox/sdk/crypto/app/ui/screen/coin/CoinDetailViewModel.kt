package cz.kotox.sdk.crypto.app.ui.screen.coin

import androidx.lifecycle.ViewModel
import cz.kotox.crypto.sdk.coindata.domain.model.CoinDetail
import cz.kotox.sdk.crypto.app.ui.CoinDetailScreenRoute
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class CoinDetailViewModel(
    val navKey: CoinDetailScreenRoute,
    // private val coinData: CoinData,
) : ViewModel() {

    val id = navKey.id

    val coinData: CoinDetail = sampleCoin

//    private val coinMarketsFlow: MutableStateFlow<List<CoinMarket>?> = MutableStateFlow(null)
//
//    val state: StateFlow<CoinsScreenState> = CoinsScreenPresenter(
//        coinMarketsFlow = coinMarketsFlow,
//    ).onStart {
//        initData()
//    }.stateInForUi(
//        scope = viewModelScope,
//        initialValue = CoinsScreenState.Loading,
//    )
//
//    private fun initData() {
//        viewModelScope.launch {
//            coinData.getCoinMarkets("usd").fold(
//                {},
//                { markets ->
//                    coinMarketsFlow.update { markets }
//                },
//            )
//        }
//    }
}
