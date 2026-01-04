@file:Suppress("MatchingDeclarationName")

package cz.kotox.sdk.crypto.app.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Immutable
data class SDKBrushes(
    val screenTopBar: Brush,
    val iconGlow: Brush,
    val bottomSheetTopBar: Brush,
)

/**
 * Factory function that creates our App Brushes based on the provided [ColorScheme].
 * This keeps the logic for gradient creation separate from the Theme composable.
 */
fun getSDKBrushes(colors: ColorScheme): SDKBrushes {
    return SDKBrushes(
        screenTopBar = Brush.verticalGradient(
            colorStops = arrayOf(
                0.0f to colors.primary, // Top: Gold
                0.85f to colors.background, // 85%: Fully Background
                1.0f to colors.background, // Bottom: Solid Background
            ),
        ),
        iconGlow = Brush.radialGradient(
            colors = listOf(
                colors.primary,
                colors.primary.copy(alpha = 0.0f),
            ),
        ),
        bottomSheetTopBar = Brush.verticalGradient(
            colorStops = arrayOf(
                0.0f to colors.primary, // Top: Solid Gold
                0.4f to colors.primary, // 40%: Still Solid Gold
                1.0f to colors.surface, // Bottom: Fade to Surface
            ),
        ),
    )
}

// Default "fallback" implementation
val UnspecifiedBrushes = SDKBrushes(
    screenTopBar = Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent)),
    iconGlow = Brush.radialGradient(listOf(Color.Transparent, Color.Transparent)),
    bottomSheetTopBar = Brush.radialGradient(listOf(Color.Transparent, Color.Transparent)),
)

val LocalSDKBrushes = staticCompositionLocalOf { UnspecifiedBrushes }
