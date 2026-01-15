package cz.kotox.crypto.sdk.news.domain

import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.error.ApiError
import cz.kotox.crypto.sdk.common.error.transformApiError
import cz.kotox.crypto.sdk.internal.common.CoroutineDispatchers
import cz.kotox.crypto.sdk.news.internal.data.api.NewsApiService
import de.jensklingenberg.ktorfit.Response
import kotlinx.coroutines.withContext

internal class NewsRequestContext(
    private val apiService: NewsApiService,
    private val dispatchers: CoroutineDispatchers,
) {

//    @Suppress("TooGenericExceptionCaught")
//    internal suspend inline fun <T> withApi(
//        crossinline action: suspend NewsApiService.() -> T,
//    ): Either<ApiError, T> = withContext(dispatchers.fetchDispatcher) {
//        try {
//            val response = action(apiService)
//            Either.Value(response)
//        } catch (exception: Exception) {
//            Either.Error(exception.transformApiError())
//        }
//    }

    @Suppress("TooGenericExceptionCaught")
    internal suspend inline fun <reified T> withApi(
        crossinline action: suspend NewsApiService.() -> Response<T>,
    ): Either<ApiError, T> = withContext(dispatchers.fetchDispatcher) {
        try {
            val result: Response<T> = action(apiService)

            if (result.isSuccessful) {
                val body = result.body()
                if (body != null) {
                    Either.Value(body) // Successfully unwrapped T
                } else {
                    Either.Error(ApiError.EmptyResponseError("Response body was null"))
                }
            } else {
                // Map HTTP status codes to specific ApiError types
                val error = when (result.code) {
                    403 -> ApiError.ForbiddenResponseError("${result.errorBody() ?: "Access Denied"}")
                    else -> ApiError.ResponseError("HTTP ${result.code}: ${result.errorBody() ?: "Unknown Error"}")
                }
                Either.Error(error)
            }
        } catch (exception: Exception) {
            // Catches connectivity issues, timeouts, or parsing errors
            Either.Error(exception.transformApiError())
        }
    }
}
