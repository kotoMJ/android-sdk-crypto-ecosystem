package cz.kotox.crypto.sdk.monitoring

import cz.kotox.crypto.sdk.common.logger.SDKLoggerCallback
import cz.kotox.crypto.sdk.internal.common.SdkDispatchers
import cz.kotox.crypto.sdk.internal.logger.SDKLoggerCallbackNoOp
import cz.kotox.crypto.sdk.monitoring.internal.MonitoringImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public open class MonitoringBuilder {
    private var networkTimeout: Duration = 10.seconds
    private var loggerCallback: SDKLoggerCallback = SDKLoggerCallbackNoOp()
    private var fetchDispatcher: CoroutineDispatcher = SdkDispatchers.fetchDispatcher

    /**
     * Set the fetch dispatcher [CoroutineDispatcher]
     */
    public fun setFetchDispatcher(dispatcher: CoroutineDispatcher): MonitoringBuilder {
        this.fetchDispatcher = dispatcher
        return this
    }

    /**
     * Set the network timeout
     * @param timeout
     */
    @JvmSynthetic
    public fun setNetworkTimeout(timeout: Duration): MonitoringBuilder {
        check(timeout > Duration.Companion.ZERO)
        this.networkTimeout = timeout
        return this
    }

    /**
     * Observe logging
     * @param sdkLoggerCallback is instance observing logs of the SDK. No operation callback is applied by default.
     */
    public fun setLoggerCallback(sdkLoggerCallback: SDKLoggerCallback): MonitoringBuilder {
        this.loggerCallback = sdkLoggerCallback
        return this
    }

    public fun build(): Monitoring = MonitoringImpl(
        config = MonitoringConfig(
            networkTimeout = networkTimeout,
            loggerCallback = loggerCallback,
        ),
        dispatcher = fetchDispatcher,
    )
}
