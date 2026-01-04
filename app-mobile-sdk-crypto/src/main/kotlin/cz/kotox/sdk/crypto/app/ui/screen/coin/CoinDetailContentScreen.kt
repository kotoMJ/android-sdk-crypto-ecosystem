package cz.kotox.sdk.crypto.app.ui.screen.coin

import android.app.Activity
import android.icu.math.BigDecimal
import android.icu.text.NumberFormat
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import coil.compose.AsyncImage
import cz.kotox.crypto.sdk.coindata.domain.model.CoinDetail
import cz.kotox.sdk.crypto.app.ui.mock.coins.coinDetailBitcoin
import cz.kotox.sdk.crypto.app.ui.theme.SDKCryptoSampleAppTheme
import cz.kotox.sdk.crypto.app.ui.theme.SDKTheme
import cz.kotox.sdk.crypto.app.ui.theme.alertShape
import cz.kotox.sdk.crypto.app.ui.theme.color.NegativeRed
import cz.kotox.sdk.crypto.app.ui.theme.color.PositiveGreen
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun CoinDetailContentScreen(
    coin: CoinDetail,
    onBackClick: () -> Unit,
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            // Force dark icons because our header is Gold
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    val scrollState = rememberScrollState()

    var showDialog by remember { mutableStateOf(false) }
    var activeInfoTitle by remember { mutableStateOf("") }
    var activeInfoDesc by remember { mutableStateOf("") }

    val onInfoClick: (String, String) -> Unit = { title, desc ->
        activeInfoTitle = title
        activeInfoDesc = desc
        showDialog = true
    }

    if (showDialog) {
        CryptoInfoDialog(
            title = activeInfoTitle,
            description = activeInfoDesc,
            onDismiss = { showDialog = false },
        )
    }

    LaunchedEffect(Unit) {
        animateScrollNudge(scrollState)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CoinDetailTopBar(onBackClick = onBackClick)
        },
        bottomBar = {
            ActionButtonsBar()
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            CoinHeader(coin)

            Spacer(modifier = Modifier.height(24.dp))

            PriceSection(
                coin = coin,
                onInfoClick = onInfoClick,
            )

            Spacer(modifier = Modifier.height(24.dp))

            ChartPlaceholder()

            TimeFrameSelector()

            Spacer(modifier = Modifier.height(24.dp))

            StatsGrid(
                coin = coin,
                onInfoClick = onInfoClick,
            )

            Spacer(modifier = Modifier.height(24.dp))

            DescriptionSection(coin)

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun CoinDetailTopBar(
    onBackClick: () -> Unit,
) {
    val topBarBrush = SDKTheme.brushes.screenTopBar
    val glowBrush = SDKTheme.brushes.iconGlow

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(topBarBrush) // Background goes Edge-to-Edge
            .statusBarsPadding(), // Content is shifted down
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp) // Standard Toolbar Height
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // --- Back Arrow with Glow ---
            IconButton(onClick = onBackClick) {
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(48.dp) // Large enough to show the glow around the icon
                            .background(glowBrush),
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }

            // --- Star Icon with Glow ---
            IconButton(onClick = { /* TODO: Toggle Favorite */ }) {
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(glowBrush),
                    )
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = "Favorite",
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}

@Composable
fun CoinHeader(coin: CoinDetail) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = coin.image.large,
            contentDescription = coin.name,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = "${coin.name} (${coin.symbol.uppercase()})",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Rank #${coin.marketCapRank ?: "N/A"} • Genesis: ${coin.genesisDate ?: "Unknown"}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
fun PriceSection(
    coin: CoinDetail,
    onInfoClick: (String, String) -> Unit,
) {
    val currency = "usd"
    val currentPrice = coin.marketData.currentPrice[currency] ?: BigDecimal.ZERO
    val priceChange = coin.marketData.priceChangePercentage24hInCurrency[currency] ?: BigDecimal.ZERO
    val isPositive = priceChange >= BigDecimal.ZERO

    Column {
        Text(
            text = formatCurrency(currentPrice),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (isPositive) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = if (isPositive) PositiveGreen else NegativeRed,
                modifier = Modifier.size(32.dp),
            )
            Text(
                text = "${priceChange.abs().toFloat()}% Today",
                style = MaterialTheme.typography.titleMedium,
                color = if (isPositive) PositiveGreen else NegativeRed,
            )

            IconButton(
                onClick = {
                    onInfoClick("Current Price", "The weighted average price across exchanges.")
                },
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(24.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Info",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                )
            }
        }
    }
}

@Composable
fun ChartPlaceholder() {
    // In Light mode: We use a subtle grey gradient or tint.
    // In Dark mode: We use the gold tint.
    val isDark = isSystemInDarkTheme()
    val gradientColor = if (isDark) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    } else {
        Color.Gray.copy(alpha = 0.1f)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp), // Increased slightly to look like a proper main chart
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        // --- DESIGN SYSTEM: Same border as StatCard & CoinListItem ---
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
        ),
    ) {
        // The gradient stays INSIDE the card to represent the "chart area"
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            gradientColor,
                            Color.Transparent,
                        ),
                    ),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Chart Visualization",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            )
        }
    }
}

@Composable
fun TimeFrameSelector() {
    val frames = listOf("24H", "2M", "11Y", "ALL")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        frames.forEachIndexed { index, frame ->
            Text(
                text = frame,
                // In Light mode, non-selected text should be darker grey (onSurfaceVariant)
                color = if (index == 0) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = if (index == 0) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}

@Composable
fun StatsGrid(
    coin: CoinDetail,
    onInfoClick: (String, String) -> Unit,
) {
    val currency = "usd"
    val data = coin.marketData

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Macro stats
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(IntrinsicSize.Max),
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Market Cap",
                value = formatCompact(data.marketCap[currency]),
                change = data.marketCapChangePercentage24hInCurrency[currency],
                description = "Total value of all coins in circulation.",
                onInfoClick = onInfoClick,
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Volume (24h)",
                value = formatCompact(data.totalVolume[currency]),
                subValue = "Traded today",
                description = "Total trading volume in the last 24h.",
                onInfoClick = onInfoClick,
            )
        }

        // Historical context
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(IntrinsicSize.Max),
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Circulating Supply",
                value = formatCompact(data.circulatingSupply),
                subValue = "${coin.symbol.uppercase()} (Active)",
                description = "Coins currently in the market.",
                onInfoClick = onInfoClick,
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Fully Diluted Val.",
                value = formatCompact(data.fullyDilutedValuation[currency]),
                subValue = "Theoretical Max Cap",
                description = "Market Cap if all coins were in circulation.",
                onInfoClick = onInfoClick,
            )
        }

        // Daily context
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(IntrinsicSize.Max),
        ) {
            // 1. All-Time High (Standard Text Card)
            StatCard(
                modifier = Modifier.weight(1f),
                title = "All-Time High",
                value = formatCompact(data.ath[currency]),
                change = data.athChangePercentage[currency],
                description = "Highest price ever recorded.",
                onInfoClick = onInfoClick,
            )

            // 2. 24h Range (Custom Progress Bar Card)
            val currentPrice = data.currentPrice[currency] ?: BigDecimal.ZERO
            val lowPrice = data.low24h[currency] ?: BigDecimal.ZERO
            val highPrice = data.high24h[currency] ?: BigDecimal.ZERO

            StatCard(
                modifier = Modifier.weight(1f),
                title = "24h Range",
                description = "Volatility range between today's lowest and highest price.",
                onInfoClick = onInfoClick,
                content = {
                    RangeProgressBar(
                        current = currentPrice,
                        low = lowPrice,
                        high = highPrice,
                    )
                },
            )
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    onInfoClick: (String, String) -> Unit,
    // Optional standard fields
    value: String? = null,
    subValue: String? = null,
    change: BigDecimal? = null,
    // Optional Custom Content
    content: (@Composable () -> Unit)? = null,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
        ),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            IconButton(
                onClick = { onInfoClick(title, description) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(32.dp)
                    .padding(8.dp),
            ) {
                // The Background Circle
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.background),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Info",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (content != null) {
                    // Render custom content (like our Progress Bar)
                    content()
                } else {
                    // Render Standard Text Content
                    Text(
                        text = value ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                    )

                    if (subValue != null) {
                        Text(
                            text = subValue,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    if (change != null) {
                        val isPos = change >= BigDecimal.ZERO
                        Text(
                            text = "${if (isPos) "+" else ""}${String.format(java.util.Locale.US, "%.2f", change.toDouble())}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isPos) PositiveGreen else NegativeRed,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RangeProgressBar(
    current: BigDecimal,
    low: BigDecimal,
    high: BigDecimal,
) {
    // 1. Calculate Progress (0.0 to 1.0)
    // Avoid division by zero
    val range = high.subtract(low)
    val progress = if (range.compareTo(BigDecimal.ZERO) == 0) {
        0.5f
    } else {
        // We use .toFloat() at the end.
        // Note: divide() requires a scale and rounding mode in ICU BigDecimal
        current.subtract(low)
            .divide(range, 4, BigDecimal.ROUND_HALF_UP) // Scale 4 gives enough precision for percentage
            .toFloat()
    }
    // Clamp values between 0.0 and 1.0 just in case
    // This handles cases where current price might be slightly outside the 24h high/low due to data sync delays
    val clampedProgress = progress.coerceIn(0f, 1f)

    Column {
        // Top Labels (Low vs High)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "L: ${formatCompact(low)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = "H: ${formatCompact(high)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // The Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)), // Track color
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(clampedProgress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.primary), // Progress color (Gold)
            )
        }
    }
}

@Composable
fun CryptoInfoDialog(
    title: String,
    description: String,
    onDismiss: () -> Unit,
) {
    val isDark = isSystemInDarkTheme()
    val borderColor = if (isDark) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }

    AlertDialog(
        modifier = Modifier.border(
            width = 1.dp,
            color = borderColor,
            shape = alertShape,
        ),
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            ) {
                Text(
                    text = "Got it",
                    fontWeight = FontWeight.Bold,
                )
            }
        },
        shape = alertShape,
    )
}

@Composable
fun DescriptionSection(coin: CoinDetail) {
    val desc = coin.description.en ?: "No description available."
    Column {
        Text(
            text = "About ${coin.name}",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = desc.replace(Regex("<.*?>"), ""),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 4,
        )
    }
}

@Composable
fun ActionButtonsBar() {
    val isDark = isSystemInDarkTheme()

    // UX DECISION:
    // In Dark Mode: Outlined Buttons can be Gold (Primary) or White (OnBackground).
    // In Light Mode: Outlined Buttons MUST be Dark Grey/Black (OnBackground) because Gold on White is invisible.
    val outlineButtonContentColor = if (isDark) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val outlineButtonBorderColor = if (isDark) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline // A nice grey border
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Buy Button (Always Gold Filled)
        Button(
            onClick = {},
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        ) {
            Text("Buy", fontWeight = FontWeight.Bold)
        }

        // Sell Button
        OutlinedButton(
            onClick = {},
            modifier = Modifier.weight(1f),
            border = androidx.compose.foundation.BorderStroke(1.dp, outlineButtonBorderColor),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = outlineButtonContentColor),
        ) {
            Text("Sell")
        }

        // Transfer Button
        OutlinedButton(
            onClick = {},
            modifier = Modifier.weight(1f),
            border = androidx.compose.foundation.BorderStroke(1.dp, outlineButtonBorderColor),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = outlineButtonContentColor),
        ) {
            Text("Transfer")
        }
    }
}

// --- Helper Functions for Formatting ---

@Suppress("TooGenericExceptionCaught", "SwallowedException")
fun formatCurrency(amount: BigDecimal?): String {
    if (amount == null) return "N/A"
    return try {
        NumberFormat.getCurrencyInstance(Locale.US).format(amount)
    } catch (e: Exception) {
        amount.toString()
    }
}

fun formatCompact(amount: BigDecimal?): String {
    if (amount == null) return "N/A"
    val doubleVal = amount.toDouble()
    return when {
        doubleVal >= 1_000_000_000 -> String.format(Locale.US, "%.2fB", doubleVal / 1_000_000_000)
        doubleVal >= 1_000_000 -> String.format(Locale.US, "%.2fM", doubleVal / 1_000_000)
        doubleVal >= 1_000 -> String.format(Locale.US, "%.2fK", doubleVal / 1_000)
        else -> String.format(Locale.US, "%.2f", doubleVal)
    }
}

/**
 * Performs a subtle "nudge" animation to indicate more content is available.
 * Safely cancels if the user interacts with the screen.
 */
@Suppress("TooGenericExceptionCaught", "SwallowedException")
suspend fun animateScrollNudge(scrollState: ScrollState) {
    // 1. Initial delay
    delay(500)

    // 2. Safety check: Don't run if user is already interacting
    if (scrollState.isScrollInProgress || scrollState.value > 0) return

    repeat(1) {
        // Check before every move
        if (scrollState.isScrollInProgress) return

        try {
            // STEP 1: Scroll DOWN (Smooth deceleration)
            // Starts fast, slows down gently at the bottom
            scrollState.animateScrollTo(
                value = 100,
                animationSpec = tween(
                    durationMillis = 300, // Slower duration for smoothness
                    easing = FastOutLinearInEasing,
                ),
            )

            // STEP 2: Scroll UP (Fast snap back)
            scrollState.animateScrollTo(
                value = 0,
                animationSpec = tween(
                    durationMillis = 200, // Faster duration (250ms) to return quickly
                    easing = FastOutSlowInEasing,
                ),
            )
        } catch (e: Exception) {
            // Stop immediately if user touches screen
            return
        }

        // Tiny pause between the two bounces
        delay(1000)
    }
}

@PreviewLightDark
@Composable
fun CoinDetailScreenPreview() {
    SDKCryptoSampleAppTheme {
        CoinDetailContentScreen(
            coin = coinDetailBitcoin,
            onBackClick = {},
        )
    }
}

@PreviewLightDark
@Composable
fun CoinDetailCryptoInfoDialog() {
    SDKCryptoSampleAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            CryptoInfoDialog(
                title = "Volume (24h)",
                description = "Total trading volume in the last 24h.",
                {},
            )
        }
    }
}

@PreviewLightDark
@Composable
fun CoinDetailCryptoDescription() {
    SDKCryptoSampleAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            DescriptionSection(
                coin = coinDetailBitcoin,
            )
        }
    }
}

// --- Mock Data for Preview ---
