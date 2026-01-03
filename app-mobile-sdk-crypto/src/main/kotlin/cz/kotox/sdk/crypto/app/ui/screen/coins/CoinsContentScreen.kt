package cz.kotox.sdk.crypto.app.ui.screen.coins

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import cz.kotox.sdk.crypto.app.R
import cz.kotox.sdk.crypto.app.ui.mock.coins.mockCoinMarkets
import cz.kotox.sdk.crypto.app.ui.theme.SDKCryptoSampleAppTheme

@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinsContentScreen(
    state: CoinsScreenState.Content,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // 1. Make the Status Bar Transparent so the Gradient shows through
            window.statusBarColor = Color.Transparent.toArgb()

            // 2. Force Status Bar Icons to be DARK (Black).
            // Since our header is Gold (Light color), we need black icons
            // even if the system is in Dark Mode.
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            val topBarBrush = Brush.verticalGradient(
                colorStops = arrayOf(
                    0.0f to MaterialTheme.colorScheme.primary, // Top: Gold
                    0.85f to MaterialTheme.colorScheme.background, // 85%: Fully Background
                    1.0f to MaterialTheme.colorScheme.background, // Bottom: Solid Background
                ),
            )

            val glowBrush = Brush.radialGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.0f),
                ),
            )

            TopAppBar(
                title = {
                    Box(contentAlignment = Alignment.Center) {
                        // The Glow Layer
                        Box(
                            modifier = Modifier
                                .size(56.dp) // Larger than the icon to spread the glow
                                .background(glowBrush),
                        )
                        // The Logo Layer
                        Icon(
                            painter = painterResource(id = R.drawable.ic_crypto),
                            contentDescription = "Kotox Crypto Logo",
                            tint = MaterialTheme.colorScheme.onPrimary, // Black Icon
                            modifier = Modifier.size(32.dp),
                        )
                    }
                },
                modifier = Modifier
                    // .height(80.dp)
                    .background(topBarBrush),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                actions = {
                    // --- Notification Icon with Glow ---
                    IconButton(onClick = { /* TODO */ }) {
                        Box(contentAlignment = Alignment.Center) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp) // Sized to fit nicely in the button touch target
                                    .background(glowBrush),
                            )
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notifications",
                            )
                        }
                    }

                    // --- Profile Icon with Glow ---
                    IconButton(onClick = { /* TODO */ }) {
                        Box(contentAlignment = Alignment.Center) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(glowBrush),
                            )
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Profile",
                            )
                        }
                    }
                },
                // windowInsets = WindowInsets(0, 0, 0, 0)
            )
        },
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp),
                ) {
                    items(
                        items = state.coinMarkets,
                        key = { it.id },
                    ) { market ->
                        CoinListItem(
                            market = market,
                            onItemClick = onItemClick,
                        )
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun CoinsContentScreenPreview() {
    SDKCryptoSampleAppTheme {
        CoinsContentScreen(
            state = CoinsScreenState.Content(
                coinMarkets = mockCoinMarkets,
            ),
            onItemClick = {},
        )
    }
}
