package cz.kotox.sdk.crypto.app.utils.security

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import cz.kotox.sdk.crypto.app.BuildConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.UUID

class SentryStore(private val context: Context) {

    private val appContext = context.applicationContext
    private val cryptoManager = CryptoManager(appContext)

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
        runBlocking { updateDsn(BuildConfig.SENTRY_DNS_CRYPTO_TRACKER_ANDROID) }
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
