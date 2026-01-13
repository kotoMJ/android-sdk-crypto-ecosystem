package cz.kotox.crypto.sdk.coindata.internal.usecase

import cz.kotox.crypto.sdk.coindata.domain.model.CoinDetail
import cz.kotox.crypto.sdk.coindata.internal.data.repository.CoinDataRepository
import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.domain.model.coin.CoinMarketId
import cz.kotox.crypto.sdk.common.error.SdkError
import kotlinx.coroutines.flow.Flow

internal class GetCoinDetailUseCase(
    private val repository: CoinDataRepository,
) {

    internal fun execute(coinMarketId: CoinMarketId): Flow<Either<SdkError, CoinDetail>> {
        return repository.getCoinDetail(coinMarketId = coinMarketId)
    }
}
