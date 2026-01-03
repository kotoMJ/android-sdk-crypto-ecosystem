package cz.kotox.sdk.crypto.app.ui.screen.currency

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import cz.kotox.sdk.crypto.app.ui.theme.SDKCryptoSampleAppTheme

@Composable
fun CurrencyContentScreen(
    modifier: Modifier = Modifier,
    onCurrencySelected: (CurrencyOption) -> Unit = {},
) {
    // This Surface provides the main shape of the bottom sheet
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // --- HEADER AREA WITH GOLD GRADIENT ---
            // This Box creates the "Header" feel, fading from Gold to Surface
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            // Use colorStops to keep the top 60% Solid Gold.
                            // This ensures the Black Text always sits on a Bright Background.
                            colorStops = arrayOf(
                                0.0f to MaterialTheme.colorScheme.primary, // Top: Solid Gold
                                0.4f to MaterialTheme.colorScheme.primary, // 30%: Still Solid Gold
                                1.0f to MaterialTheme.colorScheme.surface, // Bottom: Fade to Surface
                            ),
                        ),
                    ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp), // Space before the gradient ends
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // 1. The Drag Handle (Contrast Color)
                    // We keep this Black (onPrimary) because it sits high up in the Gold zone.
                    Box(
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .width(32.dp)
                            .height(4.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onPrimary),
                    )

                    // 2. The Title
                    Text(
                        text = "Select Currency",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        textAlign = TextAlign.Start,
                    )
                }
            }

            // --- LIST CONTENT ---
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    // Add some top padding so the list doesn't touch the title immediately
                    .padding(top = 8.dp, bottom = 32.dp),
            ) {
                items(mockCurrencies) { currency ->
                    CurrencyItem(
                        currency = currency,
                        onClick = { onCurrencySelected(currency) },
                    )
                }
            }
        }
    }
}

@Composable
fun CurrencyItem(
    currency: CurrencyOption,
    onClick: () -> Unit,
) {
    val borderColor = if (currency.isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }

    val containerColor = if (currency.isSelected) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.surface
    }

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                ) {
                    Text(
                        text = currency.symbol,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = currency.code,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = currency.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            if (currency.isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}

data class CurrencyOption(
    val code: String,
    val name: String,
    val symbol: String,
    val isSelected: Boolean = false,
)

val mockCurrencies = listOf(
    CurrencyOption("USD", "United States Dollar", "$", true),
    CurrencyOption("EUR", "Euro", "€", false),
    CurrencyOption("JPY", "Japanese Yen", "¥", false),
    CurrencyOption("GBP", "British Pound", "£", false),
    CurrencyOption("CZK", "Czech Koruna", "Kč", false),
)

@PreviewLightDark
@Composable
fun CurrencyContentScreenPreview() {
    SDKCryptoSampleAppTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            contentAlignment = Alignment.BottomCenter,
        ) {
            CurrencyContentScreen()
        }
    }
}
