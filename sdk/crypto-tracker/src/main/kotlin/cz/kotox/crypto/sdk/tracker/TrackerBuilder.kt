package cz.kotox.crypto.sdk.tracker

import cz.kotox.crypto.sdk.common.logger.SDKLoggerCallback
import cz.kotox.crypto.sdk.internal.common.CoroutineDispatchers
import cz.kotox.crypto.sdk.internal.common.SdkDispatchers
import cz.kotox.crypto.sdk.internal.logger.SDKLoggerCallbackNoOp
import cz.kotox.crypto.sdk.tracker.internal.TrackerImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public open class TrackerBuilder {
    private var fetchDispatcher: CoroutineDispatcher = SdkDispatchers.fetchDispatcher
    private var databaseDispatcher: CoroutineDispatcher = SdkDispatchers.databaseDispatcher
    private var networkTimeout: Duration = 30.seconds
    private var loggerCallback: SDKLoggerCallback = SDKLoggerCallbackNoOp()
    private var isStrictModeEnabled: Boolean = false

    /**
     * Set the fetch dispatcher [CoroutineDispatcher]
     */
    public fun setFetchDispatcher(dispatcher: CoroutineDispatcher): TrackerBuilder {
        this.fetchDispatcher = dispatcher
        return this
    }

    /**
     * Set the network timeout
     * @param timeout
     */
    @JvmSynthetic
    public fun setNetworkTimeout(timeout: Duration): TrackerBuilder {
        check(timeout > Duration.ZERO)
        this.networkTimeout = timeout
        return this
    }

    /**
     * Observe logging
     * @param sdkLoggerCallback is instance observing logs of the SDK. No operation callback is applied by default.
     */
    public fun setLoggerCallback(sdkLoggerCallback: SDKLoggerCallback): TrackerBuilder {
        this.loggerCallback = sdkLoggerCallback
        return this
    }

    /**
     * Enables non-defensive strategy: FAIL FIRST when SDK encounter any unexpected mismatch.
     * When disabled, observed mismatch is handled with defensive strategy: just logged as an error by the SDKLoggerCallback.
     */
    public fun setStrictModeEnabled(strictModeEnabled: Boolean): TrackerBuilder {
        this.isStrictModeEnabled = strictModeEnabled
        return this
    }

    public fun build(): Tracker = TrackerImpl(
        dispatchers = object : CoroutineDispatchers {
            override val fetchDispatcher: CoroutineDispatcher =
                this@TrackerBuilder.fetchDispatcher

            override val databaseDispatcher: CoroutineDispatcher =
                this@TrackerBuilder.databaseDispatcher
        },
        config = TrackerConfig(
            networkTimeout = networkTimeout,
            loggerCallback = loggerCallback,
            isStrictModeEnabled = isStrictModeEnabled,
        ),
    )
}
