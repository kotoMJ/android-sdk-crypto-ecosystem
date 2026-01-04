package cz.kotox.sdk.crypto.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import cz.kotox.sdk.crypto.app.ui.theme.SDKCryptoSampleAppTheme

class MainActivity : ComponentActivity() {

//    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        handleIntent(intent)

        setContent {
            /**
             * Help prevent leak in AndroidComposeView.composeViews
             * Force the composition to dispose exactly when the view detaches
             */
            (LocalView.current as? androidx.compose.ui.platform.AbstractComposeView)?.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
            }

            SDKCryptoSampleAppTheme {
                MainActivityContent()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        intent ?: return

        // viewModel.handleIntent(intent)
        intent.extras?.clear()
        intent.data = null
    }
}
