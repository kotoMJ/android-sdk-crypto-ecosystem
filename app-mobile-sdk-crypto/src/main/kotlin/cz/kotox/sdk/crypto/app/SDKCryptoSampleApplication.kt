package cz.kotox.sdk.crypto.app

import android.app.Application
import cz.kotox.crypto.sdk.monitoring.Monitoring
import cz.kotox.sdk.crypto.app.di.AppModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module
import timber.log.Timber

class SDKCryptoSampleApplication : Application() {
    private val monitoring: Monitoring by inject()

    override fun onCreate() {
        super.onCreate()
        setupTimber(BuildConfig.DEBUG)

        startKoin {
            androidLogger()
            androidContext(this@SDKCryptoSampleApplication)

            modules(AppModule().module)
        }

        monitoring.initMonitoring()
    }

    private fun setupTimber(isDebug: Boolean) {
        if (isDebug) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
