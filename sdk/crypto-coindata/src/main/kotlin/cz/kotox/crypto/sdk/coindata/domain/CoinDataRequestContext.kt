package cz.kotox.crypto.sdk.coindata.domain

import cz.kotox.crypto.sdk.coindata.internal.data.api.CoinDataApiService
import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.error.ApiError
import cz.kotox.crypto.sdk.common.error.transformApiError
import cz.kotox.crypto.sdk.internal.common.CoroutineDispatchers
import kotlinx.coroutines.withContext

internal class CoinDataRequestContext(
    private val apiService: CoinDataApiService,
    private val dispatchers: CoroutineDispatchers,
) {

    @Suppress("TooGenericExceptionCaught")
    internal suspend inline fun <T> withApi(
        crossinline action: suspend CoinDataApiService.() -> T,
    ): Either<ApiError, T> = withContext(dispatchers.fetchDispatcher) {
        try {
            val response = action(apiService)
            Either.Value(response)
        } catch (exception: Exception) {
            Either.Error(exception.transformApiError())
        }
    }
}
