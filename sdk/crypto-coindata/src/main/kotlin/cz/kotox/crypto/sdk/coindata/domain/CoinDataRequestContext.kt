package cz.kotox.crypto.sdk.coindata.domain

import android.database.sqlite.SQLiteException
import cz.kotox.crypto.sdk.coindata.internal.data.api.CoinDataApiService
import cz.kotox.crypto.sdk.coindata.internal.data.database.CoinDatabase
import cz.kotox.crypto.sdk.coindata.internal.utils.logE
import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.error.ApiError
import cz.kotox.crypto.sdk.common.error.transformApiError
import cz.kotox.crypto.sdk.internal.common.CoroutineDispatchers
import kotlinx.coroutines.withContext

internal class CoinDataRequestContext(
    private val apiService: CoinDataApiService,
    private val dispatchers: CoroutineDispatchers,
    private val database: CoinDatabase,
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

    internal suspend inline fun <T> withDatabase(
        logMessage: String,
        crossinline action: suspend CoinDatabase.() -> T,
    ): T? = withContext(dispatchers.databaseDispatcher) {
        try {
            action(database)
        } catch (exception: SQLiteException) {
            logE(exception) { "Content Database SQL error: $logMessage" }
            null
        }
    }
}
