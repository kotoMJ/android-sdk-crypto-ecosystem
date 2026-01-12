package cz.kotox.crypto.sdk.integrity

import android.content.Context
import cz.kotox.crypto.sdk.common.domain.model.integrity.SdkIntegrityToken

public interface Integrity {

    public class Builder(context: Context) : IntegrityBuilder(context = context)

    public suspend fun getFreshToken(): SdkIntegrityToken?

    public fun shutdown()
}
