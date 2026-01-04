package cz.kotox.sdk.crypto.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import cz.kotox.sdk.crypto.app.ui.theme.color.DarkColors
import cz.kotox.sdk.crypto.app.ui.theme.color.LightColors

@Composable
fun SDKCryptoSampleAppTheme(
    content: @Composable () -> Unit,
) {
    val darkTheme = isSystemInDarkTheme()
    val colorScheme = if (darkTheme) DarkColors else LightColors

    val brushes = remember(colorScheme) {
        getSDKBrushes(colorScheme)
    }
    CompositionLocalProvider(
        LocalSDKBrushes provides brushes,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            shapes = SDKShapes,
            typography = AppTypography,
            content = content,
        )
    }
}

// Helper object for easy access (e.g., SDKTheme.brushes.topBar)
object SDKTheme {
    val brushes: SDKBrushes
        @Composable
        @ReadOnlyComposable
        get() = LocalSDKBrushes.current
}
