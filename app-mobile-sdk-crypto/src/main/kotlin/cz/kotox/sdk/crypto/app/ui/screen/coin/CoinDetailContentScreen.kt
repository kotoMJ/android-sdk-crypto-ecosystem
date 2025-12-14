package cz.kotox.sdk.crypto.app.ui.screen.coin

import android.icu.math.BigDecimal
import android.icu.text.NumberFormat
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import cz.kotox.crypto.sdk.coindata.domain.model.CoinDetail
import cz.kotox.crypto.sdk.coindata.domain.model.Image
import cz.kotox.crypto.sdk.coindata.domain.model.Links
import cz.kotox.crypto.sdk.coindata.domain.model.Localization
import cz.kotox.crypto.sdk.coindata.domain.model.MarketData
import cz.kotox.crypto.sdk.coindata.domain.model.ReposUrl
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.time.ExperimentalTime

// --- Colors based on your dark theme screenshot ---
val DarkBackground = Color(0xFF121212)
val CardBackground = Color(0xFF1E1E1E)
val PrimaryAccent = Color(0xFFFDD835) // Gold-ish for Bitcoin
val TextWhite = Color(0xFFFFFFFF)
val TextGray = Color(0xFF888888)
val PositiveGreen = Color(0xFF4CAF50)
val NegativeRed = Color(0xFFE53935)

@Composable
fun CoinDetailContentScreen(
    coin: CoinDetail,
    onBackClick: () -> Unit,
) {
    val scrollState = rememberScrollState()

    // --- 1. HOISTED STATE (Lives here at the top level) ---
    var showDialog by remember { mutableStateOf(false) }
    var activeInfoTitle by remember { mutableStateOf("") }
    var activeInfoDesc by remember { mutableStateOf("") }

    // --- 2. SINGLE HANDLER (Passed down to children) ---
    val onInfoClick: (String, String) -> Unit = { title, desc ->
        activeInfoTitle = title
        activeInfoDesc = desc
        showDialog = true
    }

    // --- 3. THE DIALOG ITSELF (Rendered on top of everything) ---
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
        containerColor = DarkBackground,
        topBar = {
            CoinDetailTopBar(
//                coinName = coin.name,
//                coinSymbol = coin.symbol,
                onBackClick = onBackClick,
            )
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
            CoinHeader(coin)

            Spacer(modifier = Modifier.height(24.dp))

            // --- Pass handler to PriceSection ---
            PriceSection(
                coin = coin,
                onInfoClick = onInfoClick,
            )

            Spacer(modifier = Modifier.height(24.dp))

            ChartPlaceholder()

            Spacer(modifier = Modifier.height(24.dp))

            TimeFrameSelector()

            Spacer(modifier = Modifier.height(24.dp))

            // --- Pass handler to StatsGrid ---
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailTopBar(
    onBackClick: () -> Unit,
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = PrimaryAccent,
                )
            }
        },
        actions = {
            IconButton(onClick = { /* TODO: Toggle Favorite */ }) {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = "Favorite",
                    tint = TextGray,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground),
    )
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
                color = TextWhite,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Rank #${coin.marketCapRank ?: "N/A"} • Genesis: ${coin.genesisDate ?: "Unknown"}",
                style = MaterialTheme.typography.bodyMedium,
                color = TextGray,
            )
        }
    }
}

@Composable
fun PriceSection(
    coin: CoinDetail,
    onInfoClick: (String, String) -> Unit, // <-- Receives handler
) {
    val currency = "usd"
    val currentPrice = coin.marketData.currentPrice[currency] ?: BigDecimal.ZERO
    val priceChange = coin.marketData.priceChangePercentage24hInCurrency[currency] ?: BigDecimal.ZERO
    val isPositive = priceChange >= BigDecimal.ZERO

    Column {
        Text(
            text = formatCurrency(currentPrice),
            style = MaterialTheme.typography.displayMedium,
            color = TextWhite,
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

            // Invoke the handler here
            IconButton(
                onClick = {
                    onInfoClick("Current Price", "The weighted average price across exchanges.\n\nPercentage shows the change over the last rolling 24 hours.")
                },
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(24.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Info",
                    tint = TextGray.copy(alpha = 0.5f),
                )
            }
        }
    }
}

@Composable
fun ChartPlaceholder() {
    // Since CoinDetail doesn't have chart points, we render a static visual placeholder
    // representing the "Golden Line" from the screenshot.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        PrimaryAccent.copy(alpha = 0.1f),
                        Color.Transparent,
                    ),
                ),
                shape = RoundedCornerShape(16.dp),
            ),
        contentAlignment = Alignment.Center,
    ) {
        // In a real app, use a Canvas or a library like Vico/MPAndroidChart here
        Text("Chart Visualization", color = TextGray.copy(alpha = 0.3f))
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
                color = if (index == 0) TextWhite else TextGray, // Highlight first for demo
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
        // --- Row 1 ---
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Market Cap",
                value = formatCompact(data.marketCap[currency]),
                change = data.marketCapChangePercentage24hInCurrency[currency],
                description = "The total value of all coins currently in circulation.\n\n(Price × Circulating Supply)",
                onInfoClick = onInfoClick,
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Volume (24h)",
                value = formatCompact(data.totalVolume[currency]),
                subValue = "Traded today",
                description = "The total dollar value of all trades for this coin in the past 24 hours.\n\nHigh volume usually means the coin is popular and easy to sell.",
                onInfoClick = onInfoClick,
            )
        }

        // --- Row 2 ---
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Circulating Supply",
                value = formatCompact(data.circulatingSupply),
                subValue = "${coin.symbol.uppercase()} (Active)",
                description = "The amount of coins that are currently circulating in the market and are in public hands.",
                onInfoClick = onInfoClick,
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Fully Diluted Val.",
                value = formatCompact(data.fullyDilutedValuation[currency]),
                subValue = "Theoretical Max Cap",
                description = "The theoretical Market Cap if ALL possible coins (including those not yet released) were in circulation today.\n\nUseful to spot if a coin will have high inflation later.",
                onInfoClick = onInfoClick,
            )
        }

        // --- Row 3 ---
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "All Time High",
                value = formatCurrency(data.ath[currency]),
                change = data.athChangePercentage[currency],
                description = "The highest price this coin has ever reached in its entire history.",
                onInfoClick = onInfoClick,
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "All Time Low",
                value = formatCurrency(data.atl[currency]),
                change = data.atlChangePercentage[currency],
                description = "The lowest price this coin has ever reached in its entire history.",
                onInfoClick = onInfoClick,
            )
        }

        // --- Row 4 ---
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "24h High",
                value = formatCurrency(data.high24h[currency]),
                subValue = "Peak today",
                description = "The highest price reached in the last 24 hours.",
                onInfoClick = onInfoClick,
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "24h Low",
                value = formatCurrency(data.low24h[currency]),
                subValue = "Bottom today",
                description = "The lowest price reached in the last 24 hours.",
                onInfoClick = onInfoClick,
            )
        }

        // --- Optional Row 5 ---
        if (data.maxSupply != null) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Max Supply",
                    value = formatCompact(data.maxSupply),
                    subValue = "Hard Cap Limit",
                    description = "The maximum number of coins that will ever exist.\n\nOnce this limit is reached, no new coins can be created.",
                    onInfoClick = onInfoClick,
                )
                Spacer(modifier = Modifier.weight(1f))
            }
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
    description: String, // New parameter for the help text
    onInfoClick: (String, String) -> Unit, // Callback when 'i' is clicked
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp),
    ) {
        // Changed Column to Box to overlay the 'i' icon in top-right
        Box(modifier = Modifier.fillMaxSize()) {
            // The Info Icon Button
            IconButton(
                onClick = { onInfoClick(title, description) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(32.dp) // Slightly smaller touch target for the corner
                    .padding(4.dp), // Padding from the edge
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Info",
                    tint = TextGray.copy(alpha = 0.5f), // Subtle tint
                    modifier = Modifier.size(16.dp),
                )
            }

            // The content (Standard Column)
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = title, style = MaterialTheme.typography.labelLarge, color = TextGray)
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                )

                if (subValue != null) {
                    Text(text = subValue, style = MaterialTheme.typography.bodySmall, color = TextGray)
                }

                if (change != null) {
                    val isPos = change >= BigDecimal.ZERO
                    Text(
                        // Keeping your fix here
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
        containerColor = CardBackground,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = TextWhite,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                color = TextGray,
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Got it", color = PrimaryAccent)
            }
        },
        shape = RoundedCornerShape(16.dp),
    )
}

@Composable
fun DescriptionSection(coin: CoinDetail) {
    // Fallback to English description or a default message
    val desc = coin.description.en ?: "No description available."

    Column {
        Text(
            text = "About ${coin.name}",
            style = MaterialTheme.typography.titleLarge,
            color = TextWhite,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = desc.replace(Regex("<.*?>"), ""), // Simple HTML tag strip
            style = MaterialTheme.typography.bodyMedium,
            color = TextGray,
            maxLines = 4, // Truncate for UI cleanliness
        )
    }
}

@Composable
fun ActionButtonsBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Button(
            onClick = {},
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryAccent),
        ) {
            Text("Buy", color = Color.Black, fontWeight = FontWeight.Bold)
        }
        OutlinedButton(
            onClick = {},
            modifier = Modifier.weight(1f),
            border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryAccent),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryAccent),
        ) {
            Text("Sell")
        }
        OutlinedButton(
            onClick = {},
            modifier = Modifier.weight(1f),
            border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryAccent),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryAccent),
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
    CoinDetailContentScreen(
        coin = sampleCoin,
        onBackClick = {},
    )
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
