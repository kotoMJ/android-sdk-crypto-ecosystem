package cz.kotox.crypto.sdk.tracker.internal.data.api

import cz.kotox.crypto.sdk.internal.logger.SDKLogger
import cz.kotox.crypto.sdk.internal.network.KtorConfig
import cz.kotox.crypto.sdk.internal.network.KtorfitFactory
import cz.kotox.crypto.sdk.tracker.TrackerConfig
import cz.kotox.crypto.sdk.tracker.internal.dto.CoinbaseWebSocketMessage
import cz.kotox.crypto.sdk.tracker.internal.dto.TradingProductDTO
import kotlinx.coroutines.flow.Flow

internal class TrackerApiService(
    val sdkLogger: SDKLogger, // TODO MJ: can we use our own logger in KtorfitFactory?
    private val coinDataConfig: TrackerConfig,
) {

    private val ktorConfig = KtorConfig(
        baseUrl = "https://api.exchange.coinbase.com/",
        isLoggingEnabled = coinDataConfig.isLoggingEnabled,
        networkTimeout = coinDataConfig.networkTimeout,
    )

    val ktorfitFactory = KtorfitFactory(config = ktorConfig, sdkLogger = sdkLogger)

    private val coinbaseApi: CoinbaseApi = ktorfitFactory.ktorfit.createCoinbaseApi()

    private val webSocketService: CoinbaseWebSocketApi = TrackerWebSocketService(
        client = ktorfitFactory.httpClient,
    )

    /**
     * Fetches coin market data.
     */
    suspend fun getTradingProducts(): List<TradingProductDTO> {
        return coinbaseApi.getTradingProducts()
    }

    /**
     * Observes real-time messages from the WebSocket feed.
     *
     * @param productIds A list of product IDs (e.g., "BTC-USD").
     * @param channels A list of channels (e.g., "ticker").
     */
    fun observeMessages(
        productIds: List<String>,
        channels: List<String>,
    ): Flow<CoinbaseWebSocketMessage> {
        return webSocketService.observeMessages(productIds, channels)
    }
}
