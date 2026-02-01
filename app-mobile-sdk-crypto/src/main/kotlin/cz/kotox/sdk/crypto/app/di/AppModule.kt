package cz.kotox.sdk.crypto.app.di

import android.content.Context
import cz.kotox.crypto.sdk.coindata.CoinData
import cz.kotox.crypto.sdk.common.configuration.LoggingPolicy
import cz.kotox.crypto.sdk.common.configuration.NetworkLogMode
import cz.kotox.crypto.sdk.common.configuration.StrictModePolicy
import cz.kotox.crypto.sdk.common.logger.LogPriority
import cz.kotox.crypto.sdk.common.logger.SDKLoggerCallback
import cz.kotox.crypto.sdk.monitoring.Monitoring
import cz.kotox.crypto.sdk.news.News
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import timber.log.Timber

@Module
@ComponentScan("cz.kotox.sdk.crypto.app")
class AppModule {

    @Single
    fun provideCoinData(context: Context): CoinData = CoinData.Builder(context = context)
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
        .setStrictModePolicy(
            StrictModePolicy(
                strictSerialization = false,
            ),
        )
        .setLoggingPolicy(
            LoggingPolicy(
                logDatabaseQueries = false,
                networkLogMode = NetworkLogMode.BASIC,
            ),
        )
        .build()

    @Single
    fun provideNews(context: Context): News = News.Builder(context = context)
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
        .setStrictModePolicy(
            StrictModePolicy(
                strictSerialization = false,
            ),
        )
        .setLoggingPolicy(
            LoggingPolicy(
                logDatabaseQueries = false,
                networkLogMode = NetworkLogMode.BASIC,
            ),
        )
        .build()

    @Single
    fun provideMonitoring(context: Context): Monitoring = Monitoring.Builder(context = context)
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
