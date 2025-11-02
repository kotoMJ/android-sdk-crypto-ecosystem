package cz.kotox.crypto.sdk.tracker.domain

import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.error.ApiError
import cz.kotox.crypto.sdk.common.error.transformApiError
import cz.kotox.crypto.sdk.internal.common.CoroutineDispatchers
import cz.kotox.crypto.sdk.tracker.internal.data.api.TrackerApiService
import kotlinx.coroutines.withContext

internal class TrackerRequestContext(
    internal val apiService: TrackerApiService, // TODO MJ - consider not exposing apiService, but rather expose function like withApi
    private val dispatchers: CoroutineDispatchers,
) {

    @Suppress("TooGenericExceptionCaught")
    internal suspend inline fun <T> withApi(
        crossinline action: suspend TrackerApiService.() -> T,
    ): Either<ApiError, T> = withContext(dispatchers.fetchDispatcher) {
        try {
            val response = action(apiService)
            Either.Value(response)
        } catch (exception: Exception) {
            Either.Error(exception.transformApiError())
        }
    }
}
