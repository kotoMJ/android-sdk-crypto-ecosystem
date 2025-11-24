package cz.kotox.crypto.sdk.news

import cz.kotox.crypto.sdk.common.logger.SDKLoggerCallback
import cz.kotox.crypto.sdk.internal.common.CoroutineDispatchers
import cz.kotox.crypto.sdk.internal.common.SdkDispatchers
import cz.kotox.crypto.sdk.internal.logger.SDKLoggerCallbackNoOp
import cz.kotox.crypto.sdk.news.internal.NewsImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public open class NewsBuilder(
    private val newsServiceApiKey: String,
) {
    private var fetchDispatcher: CoroutineDispatcher = SdkDispatchers.fetchDispatcher
    private var databaseDispatcher: CoroutineDispatcher = SdkDispatchers.databaseDispatcher
    private var networkTimeout: Duration = 30.seconds
    private var loggerCallback: SDKLoggerCallback = SDKLoggerCallbackNoOp()
    private var isStrictModeEnabled: Boolean = false

    /**
     * Set the fetch dispatcher [CoroutineDispatcher]
     */
    public fun setFetchDispatcher(dispatcher: CoroutineDispatcher): NewsBuilder {
        this.fetchDispatcher = dispatcher
        return this
    }

    /**
     * Set the network timeout
     * @param timeout
     */
    @JvmSynthetic
    public fun setNetworkTimeout(timeout: Duration): NewsBuilder {
        check(timeout > Duration.ZERO)
        this.networkTimeout = timeout
        return this
    }

    /**
     * Observe logging
     * @param sdkLoggerCallback is instance observing logs of the SDK. No operation callback is applied by default.
     */
    public fun setLoggerCallback(sdkLoggerCallback: SDKLoggerCallback): NewsBuilder {
        this.loggerCallback = sdkLoggerCallback
        return this
    }

    /**
     * Enables non-defensive strategy: FAIL FIRST when SDK encounter any unexpected mismatch.
     * When disabled, observed mismatch is handled with defensive strategy: just logged as an error by the SDKLoggerCallback.
     */
    public fun setStrictModeEnabled(strictModeEnabled: Boolean): NewsBuilder {
        this.isStrictModeEnabled = strictModeEnabled
        return this
    }

    public fun build(): News = NewsImpl(
        dispatchers = object : CoroutineDispatchers {
            override val fetchDispatcher: CoroutineDispatcher =
                this@NewsBuilder.fetchDispatcher

            override val databaseDispatcher: CoroutineDispatcher =
                this@NewsBuilder.databaseDispatcher
        },
        config = NewsConfig(
            newsServiceApiKey = newsServiceApiKey,
            networkTimeout = networkTimeout,
            loggerCallback = loggerCallback,
            isStrictModeEnabled = isStrictModeEnabled,
        ),
    )
}
