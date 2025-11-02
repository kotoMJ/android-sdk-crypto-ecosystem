package cz.kotox.crypto.sdk.news.internal.test

import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.error.SdkError
import cz.kotox.crypto.sdk.news.domain.NewsRequestContext
import cz.kotox.crypto.sdk.news.internal.data.api.NewsApiSortBy
import cz.kotox.crypto.sdk.news.internal.dto.NewsApiResponseDTO

internal class NewsApiServiceTestImpl(
    private val newsRequestContext: NewsRequestContext,
    private val apiKey: String,
) {
    suspend fun getNews(
        query: String,
        sortBy: NewsApiSortBy = NewsApiSortBy.PUBLISHED_AT,
    ): Either<SdkError, NewsApiResponseDTO> = newsRequestContext.withApi {
        getNews(
            query = query,
            sortBy = sortBy,
            apiKey = apiKey,
        )
    }
}
