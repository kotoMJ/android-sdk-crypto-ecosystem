package cz.kotox.crypto.sdk.news.internal.data.api

import cz.kotox.crypto.sdk.internal.integrity.Integrity
import cz.kotox.crypto.sdk.internal.network.KtorConfig
import cz.kotox.crypto.sdk.internal.network.KtorfitFactory
import cz.kotox.crypto.sdk.news.NewsConfig
import cz.kotox.crypto.sdk.news.internal.dto.NewsApiResponseDTO
import de.jensklingenberg.ktorfit.Response

internal class NewsApiService(
    private val newsConfig: NewsConfig,
    private val integrity: Integrity,
) {

    private val ktorConfig = KtorConfig(
        baseUrl = "https://bff-service-1029057924274.us-central1.run.app/",
        isLoggingEnabled = newsConfig.isLoggingEnabled,
        networkTimeout = newsConfig.networkTimeout,
        strictModePolicy = newsConfig.strictModePolicy,
        loggingPolicy = newsConfig.loggingPolicy,
    )

    val ktorfitFactory = KtorfitFactory(
        config = ktorConfig,
        sdkLoggerCallback = newsConfig.loggerCallback,
        integrity = integrity,
    )

    private val newsApi: NewsApi = ktorfitFactory.ktorfit.createNewsApi()

    suspend fun getNews(
        integrityToken: String,
    ): Response<NewsApiResponseDTO> {
        return newsApi.fetchNews(
            request = NewsFetchRequest(
                integrityToken = integrityToken,
            ),
        )
    }
}
