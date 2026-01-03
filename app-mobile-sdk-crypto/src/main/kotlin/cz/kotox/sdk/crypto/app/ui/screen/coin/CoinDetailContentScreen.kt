package cz.kotox.sdk.crypto.app.ui.screen.coin

import android.app.Activity
import android.icu.math.BigDecimal
import android.icu.text.NumberFormat
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import cz.kotox.crypto.sdk.coindata.domain.model.Image
import cz.kotox.crypto.sdk.coindata.domain.model.Links
import cz.kotox.crypto.sdk.coindata.domain.model.Localization
import cz.kotox.crypto.sdk.coindata.domain.model.MarketData
import cz.kotox.crypto.sdk.coindata.domain.model.ReposUrl
import cz.kotox.sdk.crypto.app.ui.theme.SDKCryptoSampleAppTheme
import cz.kotox.sdk.crypto.app.ui.theme.color.NegativeRed
import cz.kotox.sdk.crypto.app.ui.theme.color.PositiveGreen
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.time.ExperimentalTime

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

    // Scaffold uses 'background' from theme.
    // In Light Mode, this is now Light Grey. In Dark Mode, Black.
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
    // 1. The Gradient Brush (Same as List Screen)
    val topBarBrush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to MaterialTheme.colorScheme.primary, // Top: Gold
            0.85f to MaterialTheme.colorScheme.background, // Fade to Background
            1.0f to MaterialTheme.colorScheme.background,
        ),
    )

    // 2. The Glow Brush (Behind Icons)
    val glowBrush = Brush.radialGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.0f),
        ),
    )

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
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
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
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
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
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subValue: String? = null,
    change: BigDecimal? = null,
    description: String,
    onInfoClick: (String, String) -> Unit,
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
                    .padding(4.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Info",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.size(16.dp),
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = value,
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
                        text = "${if (isPos) "+" else ""}${String.format(Locale.US, "%.2f", change.toDouble())}%",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isPos) PositiveGreen else NegativeRed,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun CryptoInfoDialog(
    title: String,
    description: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
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
            TextButton(onClick = onDismiss) {
                // In Light mode, this will be Yellow text. It's readable on White cards.
                Text("Got it", color = MaterialTheme.colorScheme.primary)
            }
        },
        shape = RoundedCornerShape(16.dp),
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
            style = MaterialTheme.typography.bodyMedium,
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

    repeat(3) {
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
            coin = sampleCoin,
            onBackClick = {},
        )
    }
}

// --- Mock Data for Preview ---

@OptIn(ExperimentalTime::class)
val sampleCoin = CoinDetail(
    id = "bitcoin",
    symbol = "BTC",
    name = "Bitcoin",
    image = Image(
        thumb = "https://assets.coingecko.com/coins/images/1/thumb/bitcoin.png",
        small = "https://assets.coingecko.com/coins/images/1/small/bitcoin.png",
        large = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png",
    ),
    marketCapRank = 1,
    coingeckoRank = 1,
    genesisDate = "2009-01-03",
    description = Localization(
        en = "Bitcoin is the first successful internet money based on peer-to-peer technology.",
        de = null,
        es = null,
        fr = null,
    ),
    links = Links(
        homepage = listOf("http://www.bitcoin.org"),
        blockchainSite = listOf(),
        officialForumUrl = listOf(),
        reposUrl = ReposUrl(github = listOf(), bitbucket = listOf()),
    ),
    platforms = emptyMap(),
    categories = listOf("Cryptocurrency"),
    assetPlatformId = null,
    lastUpdated = null,
    marketData = MarketData(
        // We use BigDecimal("String") to ensure precision in the mock
        currentPrice = mapOf("usd" to BigDecimal("45200.00")),

        // Price Change
        priceChangePercentage24hInCurrency = mapOf("usd" to BigDecimal("12.5")),
        priceChange24hInCurrency = mapOf("usd" to BigDecimal("5000.00")),

        // Market Cap
        marketCap = mapOf("usd" to BigDecimal("890000000000")), // 890B
        marketCapChangePercentage24hInCurrency = mapOf("usd" to BigDecimal("2.5")),
        marketCapChange24hInCurrency = mapOf("usd" to BigDecimal("100000")),
        marketCapRank = 1,

        // Volume
        totalVolume = mapOf("usd" to BigDecimal("35000000000")), // 35B

        // Supply
        circulatingSupply = BigDecimal("17700000"), // 17.7M
        totalSupply = BigDecimal("21000000"),
        maxSupply = BigDecimal("21000000"),

        // ATH & ATL
        ath = mapOf("usd" to BigDecimal("69000.00")),
        athChangePercentage = mapOf("usd" to BigDecimal("-34.0")),
        atl = mapOf("usd" to BigDecimal("67.81")),
        atlChangePercentage = mapOf("usd" to BigDecimal("80000.0")),

        // Null/Empty fields not used in this specific UI design
        athDate = emptyMap(),
        atlDate = emptyMap(),
        fullyDilutedValuation = emptyMap(),
        high24h = emptyMap(),
        low24h = emptyMap(),
    ),
)
