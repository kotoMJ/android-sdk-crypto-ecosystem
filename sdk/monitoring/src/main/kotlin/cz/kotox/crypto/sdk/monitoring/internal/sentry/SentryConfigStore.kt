package cz.kotox.crypto.sdk.monitoring.internal.sentry

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.UUID

internal class SentryConfigStore(private val context: Context) {

    private val appContext = context.applicationContext
    private val cryptoManager = SentryConfigManager(appContext)

    private val sentryDataStore: DataStore<SentryConfig> = DataStoreFactory.create(
        serializer = SentryConfigSerializer(cryptoManager),
        produceFile = { appContext.dataStoreFile("sentry_config.json") },
    )

    init {

        runBlocking {
            sentryDataStore.updateData { current ->
                if (current.installId.isEmpty()) {
                    current.copy(installId = UUID.randomUUID().toString())
                } else {
                    current
                }
            }
        }

        // FIXME MJ - dev hack, before BFF will be ready
        // runBlocking { updateDsn(BuildConfig.SENTRY_DNS_CRYPTO_TRACKER_ANDROID_VALUE) }
    }

    /**
     * Synchronous bridge for Application.onCreate().
     * In 2026, runBlocking for startup config is the accepted standard.
     */
    fun getInitialConfig(): SentryConfig = runBlocking {
        sentryDataStore.data.first() // Retrieves the first emission and cancels
    }

    /**
     * Updates the DSN received from your BFF.
     */
    suspend fun updateDsn(newDsn: String) {
        sentryDataStore.updateData { current ->
            current.copy(dsn = newDsn)
        }
    }
}
