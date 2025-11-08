@file:Suppress("MatchingDeclarationName")

package cz.kotox.sdk.crypto.app.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import cz.kotox.sdk.crypto.app.ui.theme.Type.bodyMedium
import cz.kotox.sdk.crypto.app.ui.theme.Type.titleLarge

@Immutable
class SDKTypography(
    val titleLarge: TextStyle,
    val bodyMedium: TextStyle,
)

internal object Type {
    val titleLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 32.sp,
    )

    val bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    )
}

val SDKAppTypography = SDKTypography(
    titleLarge = titleLarge,
    bodyMedium = bodyMedium,
)
