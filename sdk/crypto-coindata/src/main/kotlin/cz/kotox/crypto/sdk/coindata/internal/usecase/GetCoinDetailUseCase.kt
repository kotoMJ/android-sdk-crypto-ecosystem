package cz.kotox.crypto.sdk.coindata.internal.usecase

import cz.kotox.crypto.sdk.coindata.domain.CoinDataRequestContext
import cz.kotox.crypto.sdk.coindata.domain.model.CoinDetail
import cz.kotox.crypto.sdk.coindata.internal.data.mapper.toDomain
import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.domain.model.coin.CoinMarketId
import cz.kotox.crypto.sdk.common.error.SdkError

internal class GetCoinDetailUseCase(
    private val context: CoinDataRequestContext,
) {

    internal suspend fun execute(coinMarketId: CoinMarketId): Either<SdkError, CoinDetail> {
        return context.withApi {
            getCoinDetail(coinMarketId = coinMarketId).toDomain()
        }
    }
}
