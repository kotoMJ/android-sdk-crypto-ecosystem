package cz.kotox.crypto.sdk.news

import cz.kotox.crypto.sdk.common.configuration.LoggingPolicy
import cz.kotox.crypto.sdk.common.configuration.StrictModePolicy
import cz.kotox.crypto.sdk.common.logger.SDKLoggerCallback
import cz.kotox.crypto.sdk.internal.common.CoroutineDispatchers
import cz.kotox.crypto.sdk.internal.common.SdkDispatchers
import cz.kotox.crypto.sdk.internal.logger.SDKLoggerCallbackNoOp
import cz.kotox.crypto.sdk.news.internal.NewsImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public open class NewsBuilder {
    private var fetchDispatcher: CoroutineDispatcher = SdkDispatchers.fetchDispatcher
    private var databaseDispatcher: CoroutineDispatcher = SdkDispatchers.databaseDispatcher
    private var networkTimeout: Duration = 30.seconds
    private var loggerCallback: SDKLoggerCallback = SDKLoggerCallbackNoOp()
    private var strictModePolicy: StrictModePolicy = StrictModePolicy()
    private var loggingPolicy: LoggingPolicy = LoggingPolicy()

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
     * Adjust SDK strictness configuration.
     */
    public fun setStrictModePolicy(strictModePolicy: StrictModePolicy): NewsBuilder {
        this.strictModePolicy = strictModePolicy
        return this
    }

    /**
     * Adjust SDK diagnostic logging.
     */
    public fun setLoggingPolicy(loggingPolicy: LoggingPolicy): NewsBuilder {
        this.loggingPolicy = loggingPolicy
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
            networkTimeout = networkTimeout,
            loggerCallback = loggerCallback,
            strictModePolicy = strictModePolicy,
            loggingPolicy = loggingPolicy,
        ),
    )
}
