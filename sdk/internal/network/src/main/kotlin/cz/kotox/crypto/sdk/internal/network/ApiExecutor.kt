package cz.kotox.crypto.sdk.internal.network

import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.error.ApiError
import cz.kotox.crypto.sdk.common.error.transformApiError
import cz.kotox.crypto.sdk.internal.common.CoroutineDispatchers
import de.jensklingenberg.ktorfit.Response
import kotlinx.coroutines.withContext

@Suppress("TooGenericExceptionCaught")
public class ApiExecutor(val dispatchers: CoroutineDispatchers) {

    suspend inline fun <reified T> execute(
        crossinline action: suspend () -> Response<T>,
    ): Either<ApiError, T> = withContext(dispatchers.fetchDispatcher) {
        try {
            val result = action()
            if (result.isSuccessful) {
                val body = result.body()
                if (body != null) {
                    Either.Value(body)
                } else {
                    Either.Error(ApiError.EmptyResponseError("Response body was null"))
                }
            } else {
                // Here is your 403 Forbidden handling
                val error = when (result.code) {
                    403 -> ApiError.ForbiddenResponseError(result.errorBody()?.toString() ?: "Access Denied")
                    else -> ApiError.ResponseError("HTTP ${result.code}: ${result.errorBody() ?: "Unknown"}")
                }
                Either.Error(error)
            }
        } catch (e: Exception) {
            Either.Error(e.transformApiError())
        }
    }
}
