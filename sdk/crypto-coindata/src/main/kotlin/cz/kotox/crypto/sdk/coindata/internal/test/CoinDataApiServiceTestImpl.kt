package cz.kotox.crypto.sdk.coindata.internal.test

import cz.kotox.crypto.sdk.coindata.domain.CoinDataRequestContext
import cz.kotox.crypto.sdk.coindata.internal.data.dto.CoinDetailDTO
import cz.kotox.crypto.sdk.coindata.internal.data.dto.CoinMarketDTO
import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.error.SdkError

internal class CoinDataApiServiceTestImpl(
    private val coinDataRequestContext: CoinDataRequestContext,
) {
    suspend fun getUsdMarkets(): Either<SdkError, List<CoinMarketDTO>> = coinDataRequestContext.withApi {
        getMarkets(currency = "usd")
    }

    suspend fun getUsdCoinDetail(): Either<SdkError, CoinDetailDTO> = coinDataRequestContext.withApi {
        getCoinDetail(coinMarketId = "usd")
    }
}
