package cz.kotox.sdk.crypto.app.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import cz.kotox.sdk.crypto.app.ui.theme.SDKCryptoSampleAppTheme
import cz.kotox.sdk.crypto.app.ui.theme.SDKTheme

@Composable
internal fun GlowIcon(
    isSelected: Boolean,
    icon: ImageVector,
    description: String,
) {
    Box(contentAlignment = Alignment.Center) {
        // LAYER 1: The "Atmosphere" (Outer Glow)
        // Visible only when selected. Creates the "bloom" effect around the button.
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(SDKTheme.brushes.iconGlowSelected), // White/Gold Bloom
            )
        }

        // LAYER 2: The "Core" (Touch Target Anchor)
        // This is the CIRCLE you asked for. It creates a physical button shape.
        Box(
            modifier = Modifier
                .size(48.dp) // Standard touch target size
                .clip(CircleShape) // Ensures it's a perfect circle
                .background(
                    when {
                        // Case A: Selected -> Semi-solid Gold Core.
                        // Anchors the icon so it doesn't float in the glow.
                        isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)

                        // Case B: Unselected -> Subtle "Ghost" Circle.
                        // Provides contrast in Light Mode and defines the clickable area.
                        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                    },
                )
                .then(
                    Modifier.border(
                        width = 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f) else MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                        shape = CircleShape,
                    ),

                ),
            contentAlignment = Alignment.Center,
        ) {
            // LAYER 3: The Icon
            Icon(
                imageVector = icon,
                contentDescription = description,
                modifier = Modifier.size(24.dp), // Icon fits nicely inside the 48dp core
            )
        }
    }
}

@Composable
private fun GlowIconPreviewTemplate(
    marketSelected: Boolean,
    newsSelected: Boolean,
) {
    SDKCryptoSampleAppTheme {
        Box(
            modifier = Modifier
                .background(SDKTheme.brushes.screenBottomBar)
                .padding(20.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                GlowIcon(
                    isSelected = marketSelected,
                    icon = Icons.Default.Timeline,
                    description = "Markets",
                )

                GlowIcon(
                    isSelected = newsSelected,
                    icon = Icons.Default.Public,
                    description = "News",
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun GlowIconPreview_MarketsActive() {
    GlowIconPreviewTemplate(marketSelected = true, newsSelected = false)
}

@PreviewLightDark
@Composable
private fun GlowIconPreview_NewsActive() {
    GlowIconPreviewTemplate(marketSelected = false, newsSelected = true)
}
