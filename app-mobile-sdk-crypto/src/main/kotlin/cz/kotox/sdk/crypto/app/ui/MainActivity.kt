package cz.kotox.sdk.crypto.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.ComposeView
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

        /**
         * Help prevent leak in AndroidComposeView.composeViews
         * Create the ComposeView manually
         */
        val composeView = ComposeView(this).apply {
            // Set the strategy to DisposeOnViewTreeLifecycleDestroyed
            // This is the specific fix for the 'composeViews' static leak.
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                SDKCryptoSampleAppTheme {
                    MainActivityContent()
                }
            }
        }

        setContentView(composeView)
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
