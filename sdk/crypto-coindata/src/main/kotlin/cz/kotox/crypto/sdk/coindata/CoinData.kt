package cz.kotox.crypto.sdk.coindata

import android.content.Context
import cz.kotox.crypto.sdk.coindata.domain.model.CoinDetail
import cz.kotox.crypto.sdk.coindata.domain.model.CoinMarket
import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.domain.model.coin.CoinMarketId
import cz.kotox.crypto.sdk.common.domain.model.coin.CurrencyId
import cz.kotox.crypto.sdk.common.error.SdkError
import kotlinx.coroutines.flow.Flow

public interface CoinData {

    public class Builder(context: Context) : CoinDataBuilder(context = context)

    public fun getCoinMarkets(currency: CurrencyId): Flow<Either<SdkError, List<CoinMarket>>>

    public fun getCoinDetail(coinMarketId: CoinMarketId): Flow<Either<SdkError, CoinDetail>>
}
