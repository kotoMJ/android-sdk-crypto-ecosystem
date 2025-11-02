package cz.kotox.crypto.sdk.coindata.internal.usecase

import cz.kotox.crypto.sdk.coindata.domain.CoinDataRequestContext
import cz.kotox.crypto.sdk.coindata.domain.model.CoinMarket
import cz.kotox.crypto.sdk.coindata.internal.data.mapper.toDomain
import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.error.SdkError

internal class GetCoinMarketsUseCase(
    private val context: CoinDataRequestContext,
) {

    internal suspend fun execute(currency: String): Either<SdkError, List<CoinMarket>> {
        return context.withApi {
            getMarkets(currency).map { coinMarketDTO ->
                coinMarketDTO.toDomain()
            }
        }
    }
}
