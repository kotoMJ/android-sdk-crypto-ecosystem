package cz.kotox.crypto.sdk.news.internal.data.api

import cz.kotox.crypto.sdk.internal.network.KtorConfig
import cz.kotox.crypto.sdk.internal.network.KtorfitFactory
import cz.kotox.crypto.sdk.news.NewsConfig
import cz.kotox.crypto.sdk.news.internal.dto.NewsApiResponseDTO

internal class NewsApiService(
    private val newsConfig: NewsConfig,
) {

    private val ktorConfig = KtorConfig(
        baseUrl = "https://newsapi.org/",
        isLoggingEnabled = newsConfig.isLoggingEnabled,
        networkTimeout = newsConfig.networkTimeout,
        isStrictModeEnabled = newsConfig.isStrictModeEnabled,
    )

    val ktorfitFactory = KtorfitFactory(
        config = ktorConfig,
        sdkLoggerCallback = newsConfig.loggerCallback,
    )

    private val newsApi: NewsApi = ktorfitFactory.ktorfit.createNewsApi()

    suspend fun getNews(
        query: String,
        sortBy: NewsApiSortBy,
        apiKey: String,
    ): NewsApiResponseDTO {
        return newsApi.getNews(
            query = query,
            sortBy = sortBy,
            apiKey = apiKey,
        )
    }
}
