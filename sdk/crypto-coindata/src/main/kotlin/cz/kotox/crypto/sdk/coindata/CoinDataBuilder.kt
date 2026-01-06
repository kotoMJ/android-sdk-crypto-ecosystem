package cz.kotox.crypto.sdk.coindata

import android.content.Context
import cz.kotox.crypto.sdk.coindata.internal.CoinDataImpl
import cz.kotox.crypto.sdk.common.configuration.LoggingPolicy
import cz.kotox.crypto.sdk.common.configuration.StrictModePolicy
import cz.kotox.crypto.sdk.common.logger.SDKLoggerCallback
import cz.kotox.crypto.sdk.internal.common.CoroutineDispatchers
import cz.kotox.crypto.sdk.internal.common.SdkDispatchers
import cz.kotox.crypto.sdk.internal.logger.SDKLoggerCallbackNoOp
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public open class CoinDataBuilder(
    private val context: Context,
) {
    private var fetchDispatcher: CoroutineDispatcher = SdkDispatchers.fetchDispatcher
    private var databaseDispatcher: CoroutineDispatcher = SdkDispatchers.databaseDispatcher
    private var networkTimeout: Duration = 30.seconds
    private var loggerCallback: SDKLoggerCallback = SDKLoggerCallbackNoOp()
    private var strictModePolicy: StrictModePolicy = StrictModePolicy()
    private var loggingPolicy: LoggingPolicy = LoggingPolicy()

    /**
     * Set the fetch dispatcher [CoroutineDispatcher]
     */
    public fun setFetchDispatcher(dispatcher: CoroutineDispatcher): CoinDataBuilder {
        this.fetchDispatcher = dispatcher
        return this
    }

    /**
     * Set the network timeout
     * @param timeout
     */
    @JvmSynthetic
    public fun setNetworkTimeout(timeout: Duration): CoinDataBuilder {
        check(timeout > Duration.ZERO)
        this.networkTimeout = timeout
        return this
    }

    /**
     * Observe logging
     * @param sdkLoggerCallback is instance observing logs of the SDK. No operation callback is applied by default.
     */
    public fun setLoggerCallback(sdkLoggerCallback: SDKLoggerCallback): CoinDataBuilder {
        this.loggerCallback = sdkLoggerCallback
        return this
    }

    /**
     * Adjust SDK strictness configuration.
     */
    public fun setStrictModePolicy(strictModePolicy: StrictModePolicy): CoinDataBuilder {
        this.strictModePolicy = strictModePolicy
        return this
    }

    /**
     * Adjust SDK diagnostic logging.
     */
    public fun setLoggingPolicy(loggingPolicy: LoggingPolicy): CoinDataBuilder {
        this.loggingPolicy = loggingPolicy
        return this
    }

    public fun build(): CoinData = CoinDataImpl(
        dispatchers = object : CoroutineDispatchers {
            override val fetchDispatcher: CoroutineDispatcher =
                this@CoinDataBuilder.fetchDispatcher

            override val databaseDispatcher: CoroutineDispatcher =
                this@CoinDataBuilder.databaseDispatcher
        },
        config = CoinDataConfig(
            networkTimeout = networkTimeout,
            loggerCallback = loggerCallback,
            strictModePolicy = strictModePolicy,
            loggingPolicy = loggingPolicy,
        ),
        context = context,
    )
}
