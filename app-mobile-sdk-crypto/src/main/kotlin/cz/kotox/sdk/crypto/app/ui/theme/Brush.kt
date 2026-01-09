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
    val screenBottomBar: Brush,
    val iconGlow: Brush,
    val iconGlowSelected: Brush,
    val iconGlowUnselected: Brush,
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
        screenBottomBar = Brush.verticalGradient(
            colorStops = arrayOf(
                0.0f to colors.background, // Top: Matches list background (Seamless)
                0.35f to colors.background, // Keep it black for a bit to ensure readability
                1.0f to colors.primary, // Bottom: Solid Gold
            ),
        ),
        iconGlow = Brush.radialGradient(
            colors = listOf(
                colors.primary,
                colors.primary.copy(alpha = 0.0f),
            ),
        ),
        // 1. SELECTED GLOW: Bright White center fading to Gold.
        // This makes it "lighter than the gold behind".
        iconGlowSelected = Brush.radialGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.6f), // Bright center
                colors.primary.copy(alpha = 0.6f), // Gold halo
                colors.primary.copy(alpha = 0.0f), // Fade out
            ),
        ),
        // 2. UNSELECTED GLOW: Dark Black scrim.
        // This blocks the "fog" so the icon stands out.
        iconGlowUnselected = Brush.radialGradient(
            colors = listOf(
                colors.background.copy(alpha = 0.9f), // Solid Black center
                colors.background.copy(alpha = 0.6f),
                colors.background.copy(alpha = 0.0f), // Fade out
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
    screenBottomBar = Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent)),
    iconGlow = Brush.radialGradient(listOf(Color.Transparent, Color.Transparent)),
    iconGlowSelected = Brush.radialGradient(listOf(Color.Transparent, Color.Transparent)),
    iconGlowUnselected = Brush.radialGradient(listOf(Color.Transparent, Color.Transparent)),
    bottomSheetTopBar = Brush.radialGradient(listOf(Color.Transparent, Color.Transparent)),
)

val LocalSDKBrushes = staticCompositionLocalOf { UnspecifiedBrushes }
