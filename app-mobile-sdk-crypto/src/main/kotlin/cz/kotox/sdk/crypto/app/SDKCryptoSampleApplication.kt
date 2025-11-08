package cz.kotox.sdk.crypto.app

import android.app.Application
import cz.kotox.crypto.sdk.coindata.BuildConfig
import timber.log.Timber

class SDKCryptoSampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupTimber(BuildConfig.DEBUG)
    }

    private fun setupTimber(isDebug: Boolean) {
        if (isDebug) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
