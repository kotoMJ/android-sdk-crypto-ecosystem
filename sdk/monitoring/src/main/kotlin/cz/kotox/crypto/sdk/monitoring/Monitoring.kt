package cz.kotox.crypto.sdk.monitoring

import android.content.Context

public interface Monitoring {

    public class Builder(context: Context) : MonitoringBuilder(context = context)

//    /**
//     * Fetches the current token for a network request
//     *
//     * @param uniqueRequestHash - is specifically designed to bind the integrity verdict to the content of your network request,
//     * ensuring the data hasn't been tampered with in transit.
//     * If you reuse the same hash (or the same token) for different requests,
//     * a malicious actor could intercept a "good" token from a news request and try to reuse it to bypass security on a more sensitive action.
//     */
//    public suspend fun getFreshToken(uniqueRequestHash: String): SdkIntegrityToken?
//
//    public fun getIntegrityHash(content: String): String
//
//    public fun getSecurityHeader(): SdkSecurityHeader?

    public fun shutdown()
}
