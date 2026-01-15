package cz.kotox.crypto.sdk.news.domain

import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.error.ApiError
import cz.kotox.crypto.sdk.internal.network.ApiExecutor
import cz.kotox.crypto.sdk.news.internal.data.api.NewsApiService
import de.jensklingenberg.ktorfit.Response

internal class NewsRequestContext(
    private val apiService: NewsApiService,
    private val apiExecutor: ApiExecutor,
) {
    internal suspend inline fun <reified T> withApi(
        crossinline action: suspend NewsApiService.() -> Response<T>,
    ): Either<ApiError, T> {
        return apiExecutor.execute { action(apiService) }
    }
}
