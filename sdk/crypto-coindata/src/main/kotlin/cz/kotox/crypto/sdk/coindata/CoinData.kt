package cz.kotox.crypto.sdk.coindata

import cz.kotox.crypto.sdk.coindata.domain.model.CoinDetail
import cz.kotox.crypto.sdk.coindata.domain.model.CoinMarket
import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.error.SdkError

public interface CoinData {

    public class Builder : CoinDataBuilder()

    public suspend fun getCoinMarkets(currency: String): Either<SdkError, List<CoinMarket>>

    public suspend fun getCoinDetail(coinMarketId: String): Either<SdkError, CoinDetail>
}
