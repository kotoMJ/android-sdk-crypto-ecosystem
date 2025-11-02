package cz.kotox.crypto.sdk.news.domain

import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.error.ApiError
import cz.kotox.crypto.sdk.common.error.transformApiError
import cz.kotox.crypto.sdk.internal.common.CoroutineDispatchers
import cz.kotox.crypto.sdk.news.internal.data.api.NewsApiService
import kotlinx.coroutines.withContext

internal class NewsRequestContext(
    private val apiService: NewsApiService,
    private val dispatchers: CoroutineDispatchers,
) {

    @Suppress("TooGenericExceptionCaught")
    internal suspend inline fun <T> withApi(
        crossinline action: suspend NewsApiService.() -> T,
    ): Either<ApiError, T> = withContext(dispatchers.fetchDispatcher) {
        try {
            val response = action(apiService)
            Either.Value(response)
        } catch (exception: Exception) {
            Either.Error(exception.transformApiError())
        }
    }
}
