package cz.kotox.sdk.crypto.app.ui.component

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext

@Immutable
data class SystemUiParams(
    val statusBarColor: Color = Color.Transparent,
    val navigationBarColor: Color? = null,
    val statusBarDarkIcons: Boolean? = null,
    val navigationBarDarkIcons: Boolean? = null,
)

/**
 * The default light scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

/**
 * The default dark scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)

@Composable
fun SystemUiController(systemUiParams: SystemUiParams) {
    val context = LocalContext.current
    val isDarkTheme = isSystemInDarkTheme()
    val activity = remember { context.getActivity() } ?: return

    SideEffect {
        activity.enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = systemUiParams.statusBarColor.toArgb(),
                darkScrim = systemUiParams.statusBarColor.toArgb(),
            ) { systemUiParams.statusBarDarkIcons?.not() ?: isDarkTheme },
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = systemUiParams.navigationBarColor?.toArgb() ?: lightScrim,
                darkScrim = systemUiParams.navigationBarColor?.toArgb() ?: darkScrim,
            ) { systemUiParams.navigationBarDarkIcons?.not() ?: isDarkTheme },
        )
    }
}

private fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}
