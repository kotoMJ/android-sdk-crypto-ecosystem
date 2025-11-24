package cz.kotox.crypto.sdk.news.internal

import cz.kotox.crypto.sdk.internal.common.CoroutineDispatchers
import cz.kotox.crypto.sdk.internal.logger.SDKLogger
import cz.kotox.crypto.sdk.internal.logger.SDKLoggerCallbackNoOp
import cz.kotox.crypto.sdk.news.MODULE_IDENTIFIER
import cz.kotox.crypto.sdk.news.News
import cz.kotox.crypto.sdk.news.NewsConfig
import cz.kotox.crypto.sdk.news.domain.NewsRequestContext
import cz.kotox.crypto.sdk.news.internal.data.api.NewsApiService
import cz.kotox.crypto.sdk.news.internal.test.NewsApiServiceTestImpl

internal class NewsImpl(
    private val config: NewsConfig,
    private val dispatchers: CoroutineDispatchers,
) : News {

    init {
        installLogger(config)
    }

//    // Keep this lazy so we can init logger first.
//    private val logger by lazy {
//        SDKLogger.getLogger(MODULE_IDENTIFIER)
//    }

    // TODO MJ - make it private whenever used internally
    internal val newsRequestContext: NewsRequestContext by lazy {
        NewsRequestContext(
            apiService = provideTrackerApiService(),
            dispatchers = dispatchers,
        )
    }

    internal val apiTestDelegate by lazy {
        NewsApiServiceTestImpl(
            newsRequestContext = newsRequestContext,
            apiKey = config.newsServiceApiKey,
        )
    }

//    override suspend fun getCoinMarkets(): Either<SdkError, List<CoinMarket>> =
//        GetCoinMarkets(
//            context = coinDataRequestContext,
//        ).execute()

    private fun provideTrackerApiService(): NewsApiService = NewsApiService(
        newsConfig = config,
    )

    private fun installLogger(config: NewsConfig) {
        if (config.loggerCallback is SDKLoggerCallbackNoOp) {
            SDKLogger.uninstall(MODULE_IDENTIFIER)
        } else {
            SDKLogger.install(tag = MODULE_IDENTIFIER, loggerCallback = config.loggerCallback)
        }
    }
}
