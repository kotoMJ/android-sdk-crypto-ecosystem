@file:Suppress("MatchingDeclarationName")

package cz.kotox.sdk.crypto.app.ui.preview

import androidx.compose.runtime.Composable
import cz.kotox.sdk.crypto.app.ui.theme.SDKCryptoSampleAppTheme

@Composable
fun SDKSampleAppThemePreview(
    content: @Composable () -> Unit,
) {
    SDKCryptoSampleAppTheme(
        content = content,
    )
}
