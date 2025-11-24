package cz.kotox.crypto.sdk.coindata

import cz.kotox.crypto.sdk.coindata.internal.CoinDataImpl
import cz.kotox.crypto.sdk.common.logger.SDKLoggerCallback
import cz.kotox.crypto.sdk.internal.common.CoroutineDispatchers
import cz.kotox.crypto.sdk.internal.common.SdkDispatchers
import cz.kotox.crypto.sdk.internal.logger.SDKLoggerCallbackNoOp
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public open class CoinDataBuilder {
    private var fetchDispatcher: CoroutineDispatcher = SdkDispatchers.fetchDispatcher
    private var databaseDispatcher: CoroutineDispatcher = SdkDispatchers.databaseDispatcher
    private var networkTimeout: Duration = 30.seconds
    private var loggerCallback: SDKLoggerCallback = SDKLoggerCallbackNoOp()
    private var isStrictModeEnabled: Boolean = false

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
     * Enables non-defensive strategy: FAIL FIRST when SDK encounter any unexpected mismatch.
     * When disabled, observed mismatch is handled with defensive strategy: just logged as an error by the SDKLoggerCallback.
     */
    public fun setStrictModeEnabled(strictModeEnabled: Boolean): CoinDataBuilder {
        this.isStrictModeEnabled = strictModeEnabled
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
            isStrictModeEnabled = isStrictModeEnabled,
        ),
    )
}
