package cz.kotox.sdk.crypto.app.ui.screen.coins

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import cz.kotox.crypto.sdk.coindata.domain.model.CoinMarket
import cz.kotox.sdk.crypto.app.R
import cz.kotox.sdk.crypto.app.ui.mock.coins.mockCoinMarkets
import cz.kotox.sdk.crypto.app.ui.theme.SDKCryptoSampleAppTheme

@Composable
fun CoinListItem(
    market: CoinMarket,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val percentageColor = if ((market.priceChangePercentage24h ?: 0.0) >= 0.0) {
        Color(0xFF34C759) // Green
    } else {
        Color(0xFFFF3B30) // Red
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        onClick = { onItemClick(market.id) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = market.imageUrl,
                placeholder = painterResource(id = R.drawable.ic_progress),
                error = painterResource(id = R.drawable.ic_crypto),
                contentDescription = "${market.name} Icon",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = market.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    // text = formatMarketCap(market.marketCap),
                    text = market.symbol.uppercase(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = formatCurrency(market.currentPrice),
                    style = MaterialTheme.typography.bodyMedium,
                    // This yellow is a semantic color (not theme-based)
                    // color = Color(0xFFFFD700),
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = formatPercentage(market.priceChangePercentage24h ?: 0.0),
                    style = MaterialTheme.typography.bodyMedium,
                    color = percentageColor,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun CoinListItemPreview() {
    SDKCryptoSampleAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            CoinListItem(
                market = mockCoinMarkets.first(),
                onItemClick = {},
            )
        }
    }
}
