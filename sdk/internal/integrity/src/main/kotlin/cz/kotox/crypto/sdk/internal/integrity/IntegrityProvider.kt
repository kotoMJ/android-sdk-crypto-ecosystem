package cz.kotox.crypto.sdk.internal.integrity

import android.content.Context
import com.google.android.play.core.integrity.IntegrityManagerFactory
import com.google.android.play.core.integrity.StandardIntegrityManager
import cz.kotox.crypto.sdk.internal.integrity.utils.logE
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.seconds

internal class IntegrityProvider(context: Context) {
    private val standardIntegrityManager: StandardIntegrityManager =
        IntegrityManagerFactory.createStandard(context.applicationContext)

    // Use your 12-digit static Cloud Project Number
    private val cloudProjectNumber = 1029057924274L

    private val tokenProviderDeferred = CompletableDeferred<StandardIntegrityManager.StandardIntegrityTokenProvider>()

    /**
     * Call this during SDK initialization to warm up the provider
     */
    @Suppress("TooGenericExceptionCaught")
    suspend fun prepare() {
        try {
            val request = StandardIntegrityManager.PrepareIntegrityTokenRequest.builder()
                .setCloudProjectNumber(cloudProjectNumber)
                .build()

            val provider = standardIntegrityManager.prepareIntegrityToken(request).await()
            tokenProviderDeferred.complete(provider) // Signal that we are ready!
        } catch (e: Exception) {
            logE(e, { "Unable to initialize play integrity!" })
        }
    }

    /**
     * Fetches the current token for a network request
     *
     * @param uniqueRequestHash - is specifically designed to bind the integrity verdict to the content of your network request,
     * ensuring the data hasn't been tampered with in transit.
     * If you reuse the same hash (or the same token) for different requests,
     * a malicious actor could intercept a "good" token from a news request and try to reuse it to bypass security on a more sensitive action.
     */
    @Suppress("TooGenericExceptionCaught")
    suspend fun getIntegrityToken(uniqueRequestHash: String): String? {
        return try {
            // Wait for init to finish with a reasonable timeout (e.g., 10s)
            val provider = withTimeoutOrNull(10.seconds) {
                tokenProviderDeferred.await()
            } ?: return null

            val response = provider.request(
                StandardIntegrityManager.StandardIntegrityTokenRequest.builder()
                    .setRequestHash(uniqueRequestHash)
                    .build(),
            ).await()
            response.token()
        } catch (e: Exception) {
            logE(e, { "Unable to obtain play integrity token!" })
            null
        }
    }
}
