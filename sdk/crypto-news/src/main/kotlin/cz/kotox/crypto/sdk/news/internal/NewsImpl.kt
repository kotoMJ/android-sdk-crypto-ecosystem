package cz.kotox.crypto.sdk.news.internal

import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.error.SdkError
import cz.kotox.crypto.sdk.internal.common.CoroutineDispatchers
import cz.kotox.crypto.sdk.internal.integrity.Integrity
import cz.kotox.crypto.sdk.internal.logger.SDKLogger
import cz.kotox.crypto.sdk.internal.logger.SDKLoggerCallbackNoOp
import cz.kotox.crypto.sdk.internal.network.ApiExecutor
import cz.kotox.crypto.sdk.news.MODULE_IDENTIFIER
import cz.kotox.crypto.sdk.news.News
import cz.kotox.crypto.sdk.news.NewsConfig
import cz.kotox.crypto.sdk.news.domain.Article
import cz.kotox.crypto.sdk.news.domain.NewsRequestContext
import cz.kotox.crypto.sdk.news.internal.data.api.NewsApiService
import cz.kotox.crypto.sdk.news.internal.data.repository.NewsRepository
import cz.kotox.crypto.sdk.news.internal.test.NewsApiServiceTestImpl
import cz.kotox.crypto.sdk.news.internal.usecase.GetArticlesUseCase

internal class NewsImpl(
    private val config: NewsConfig,
    private val dispatchers: CoroutineDispatchers,
    private val integrity: Integrity,
) : News {

    init {
        installLogger(config)
    }

    private lateinit var repository: NewsRepository

//    // Keep this lazy so we can init logger first.
//    private val logger by lazy {
//        SDKLogger.getLogger(MODULE_IDENTIFIER)
//    }

    // TODO MJ - make it private whenever used internally
    internal val newsRequestContext: NewsRequestContext by lazy {
        NewsRequestContext(
            apiService = provideTrackerApiService(),
            apiExecutor = ApiExecutor(dispatchers),
        )
    }

    internal val apiTestDelegate by lazy {
        NewsApiServiceTestImpl(
            newsRequestContext = newsRequestContext,
        )
    }

    private fun provideTrackerApiService(): NewsApiService = NewsApiService(
        newsConfig = config,
        integrity = integrity,
    )

    private fun installLogger(config: NewsConfig) {
        if (config.loggerCallback is SDKLoggerCallbackNoOp) {
            SDKLogger.uninstall(MODULE_IDENTIFIER)
        } else {
            SDKLogger.install(tag = MODULE_IDENTIFIER, loggerCallback = config.loggerCallback)
        }
    }

    private fun provideRepository(): NewsRepository {
        synchronized(NewsRepository::class.java) {
            if (!::repository.isInitialized) {
                repository = NewsRepository(
                    requestContext = newsRequestContext,
                    integrity = integrity,
                )
            }
        }

        return repository
    }

    override suspend fun getNews(): Either<SdkError, List<Article>> =
        GetArticlesUseCase(
            repository = provideRepository(),
        ).execute()
}
