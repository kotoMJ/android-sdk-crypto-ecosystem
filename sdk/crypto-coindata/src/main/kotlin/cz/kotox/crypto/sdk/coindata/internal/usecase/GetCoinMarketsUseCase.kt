package cz.kotox.crypto.sdk.coindata.internal.usecase

import cz.kotox.crypto.sdk.coindata.domain.model.CoinMarket
import cz.kotox.crypto.sdk.coindata.internal.data.database.repository.CoinDataRepository
import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.domain.model.coin.CurrencyId
import cz.kotox.crypto.sdk.common.error.SdkError
import kotlinx.coroutines.flow.Flow

internal class GetCoinMarketsUseCase(
    private val repository: CoinDataRepository,
) {

    internal fun execute(currency: CurrencyId): Flow<Either<SdkError, List<CoinMarket>>> {
        return repository.getCoinMarkets(currency = currency)
    }
}
