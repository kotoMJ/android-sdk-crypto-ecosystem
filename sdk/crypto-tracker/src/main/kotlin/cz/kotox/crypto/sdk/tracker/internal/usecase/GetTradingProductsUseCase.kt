package cz.kotox.crypto.sdk.tracker.internal.usecase

import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.error.SdkError
import cz.kotox.crypto.sdk.tracker.domain.TrackerRequestContext
import cz.kotox.crypto.sdk.tracker.domain.model.TradingProduct
import cz.kotox.crypto.sdk.tracker.internal.mapper.toDomain

internal class GetTradingProductsUseCase(
    private val context: TrackerRequestContext,
) {

    internal suspend fun execute(): Either<SdkError, List<TradingProduct>> {
        return context.withApi {
            getTradingProducts().map { coinMarketDTO ->
                coinMarketDTO.toDomain()
            }
        }
    }
}
