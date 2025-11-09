package cz.kotox.sdk.crypto.app.di

import cz.kotox.crypto.sdk.coindata.CoinData
import cz.kotox.crypto.sdk.common.logger.LogPriority
import cz.kotox.crypto.sdk.common.logger.SDKLoggerCallback
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import timber.log.Timber

@Module
@ComponentScan("cz.kotox.sdk.crypto.app")
class AppModule {

    @Single
    fun provideCoinData(): CoinData = CoinData.Builder()
        .setLoggerCallback(
            sdkLoggerCallback = object : SDKLoggerCallback {
                override fun onLogMessage(
                    tag: String,
                    priority: LogPriority,
                    t: Throwable?,
                    message: String,
                ) {
                    Timber.tag(tag)
                    Timber.log(priority = priority.priorityInt, t = t, message = message)
                }
            },
        )
        .build()
}
