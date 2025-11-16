package cz.kotox.sdk.crypto.app.ui.screen.coin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import cz.kotox.sdk.crypto.app.ui.theme.SDKCryptoSampleAppTheme

@Composable
fun CoinDetailContentScreen(
    id: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("DETAIL: $id")
        }
    }
}

@PreviewLightDark
@Composable
fun CoinsContentScreenPreview() {
    SDKCryptoSampleAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            CoinDetailContentScreen(
                id = "123",
            )
        }
    }
}
