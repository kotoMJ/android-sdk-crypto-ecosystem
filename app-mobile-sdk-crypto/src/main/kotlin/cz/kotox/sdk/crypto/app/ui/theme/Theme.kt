package cz.kotox.sdk.crypto.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cz.kotox.sdk.crypto.app.ui.theme.color.DarkColors
import cz.kotox.sdk.crypto.app.ui.theme.color.LightColors

@Composable
fun SDKCryptoSampleAppTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) {
            DarkColors
        } else {
            LightColors
        },
        shapes = SDKShapes,
        content = content,
    )
}
