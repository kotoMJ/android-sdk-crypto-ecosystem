package cz.kotox.sdk.crypto.app.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import cz.kotox.sdk.crypto.app.ui.theme.SDKTheme

@Composable
fun CryptoBottomBar(
    currentRoute: NavKey?,
    onNavigate: (NavKey) -> Unit,
    marketRoute: NavKey,
    newsRoute: NavKey,
) {
    val bottomBarBrush = SDKTheme.brushes.screenBottomBar

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(bottomBarBrush) // <--- The Gold Gradient
            .navigationBarsPadding(), // Handle safe area
    ) {
        NavigationBar(
            containerColor = Color.Transparent, // Make container transparent to show the brush
            tonalElevation = 0.dp,
            modifier = Modifier
                .height(80.dp),
            windowInsets = WindowInsets(0, 0, 0, 0), // We handle insets manually in the Box
        ) {
            // Markets Tab
            val isMarketSelected = currentRoute == marketRoute
            NavigationBarItem(
                selected = isMarketSelected,
                onClick = { onNavigate(marketRoute) },
                icon = {
                    GlowIcon(
                        isSelected = isMarketSelected,
                        icon = Icons.Default.Timeline,
                        description = "Markets",
                    )
                },
                colors = navigationItemColors(),
            )

            // News Tab
            val isNewsSelected = currentRoute == newsRoute
            NavigationBarItem(
                selected = isNewsSelected,
                onClick = { onNavigate(newsRoute) },
                icon = {
                    GlowIcon(
                        isSelected = isNewsSelected,
                        icon = Icons.Default.Public,
                        description = "News",
                    )
                },
                colors = navigationItemColors(),
            )
        }
    }
}

@Composable
private fun navigationItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = MaterialTheme.colorScheme.onPrimary, // Icon color when selected (Black/White depending on theme)
    selectedTextColor = MaterialTheme.colorScheme.primary, // Text color when selected (Gold)
    indicatorColor = Color.Transparent, // Remove the default pill/oval
    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
)

@Composable
private fun GlowIcon(
    isSelected: Boolean,
    icon: ImageVector,
    description: String,
) {
    Box(contentAlignment = Alignment.Center) {
        // ALWAYS show a background glow/scrim
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(
                    // Switch brush based on state
                    if (isSelected) {
                        SDKTheme.brushes.iconGlowSelected
                    } else {
                        SDKTheme.brushes.iconGlowUnselected
                    },
                ),
        )

        // The Icon sits on top
        Icon(
            imageVector = icon,
            contentDescription = description,
            modifier = Modifier.size(26.dp),
            // We do NOT tint here, letting NavigationBarItemDefaults handle colors
            // (Gold for selected, Grey for unselected)
        )
    }
}
