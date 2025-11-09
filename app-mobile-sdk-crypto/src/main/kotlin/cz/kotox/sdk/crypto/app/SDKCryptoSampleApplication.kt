package cz.kotox.sdk.crypto.app

import android.app.Application
import cz.kotox.crypto.sdk.coindata.BuildConfig
import cz.kotox.sdk.crypto.app.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module
import timber.log.Timber

class SDKCryptoSampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupTimber(BuildConfig.DEBUG)

        startKoin {
            androidLogger()
            androidContext(this@SDKCryptoSampleApplication)

            modules(AppModule().module)
        }
    }

    private fun setupTimber(isDebug: Boolean) {
        if (isDebug) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
