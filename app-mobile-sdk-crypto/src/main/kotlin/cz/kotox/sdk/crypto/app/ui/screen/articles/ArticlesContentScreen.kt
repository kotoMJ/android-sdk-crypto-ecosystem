package cz.kotox.sdk.crypto.app.ui.screen.articles

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
import androidx.compose.material.icons.automirrored.outlined.Sort
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.outlined.Sort
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import cz.kotox.crypto.sdk.news.domain.Article
import cz.kotox.crypto.sdk.news.domain.Source
import cz.kotox.sdk.crypto.app.R
import cz.kotox.sdk.crypto.app.ui.theme.SDKCryptoSampleAppTheme
import cz.kotox.sdk.crypto.app.ui.theme.SDKTheme
import kotlin.time.Instant

@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticlesContentScreen(
    onItemClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    // --- Mock Data matching your Article domain model ---
    val articles = listOf(
        Article(
            source = Source(null, "Cointelegraph"),
            author = "Nate Kostar",
            title = "Riot Platforms vendió USD 161M en BTC tras ajustar su estrategia corporativa",
            description = "El minero de Bitcoin vendió 1.818 BTC...",
            url = "https://es.cointelegraph.com/news/...",
            urlToImage = "https://images.cointelegraph.com/cdn-cgi/image/f=auto,onerror=redirect,w=1200/https://s3.cointelegraph.com/uploads/2026-01/019b990c-3be0-703a-b6fa-35624b794743.jpg",
            publishedAt = Instant.parse("2026-01-07T17:55:00Z"),
            content = "Full content...",
        ),
        Article(
            source = Source(null, "Criptonoticias"),
            author = "Bárbara Distéfano",
            title = "Bitcoin está subiendo, pero… ¿qué tanta fuerza tiene?",
            description = "Con el alza que trajo el inicio del 2026...",
            url = "https://www.criptonoticias.com/...",
            urlToImage = "https://www.criptonoticias.com/wp-content/uploads/2025/04/bitcoin-precio-analisis-analista.jpg",
            publishedAt = Instant.parse("2026-01-07T16:39:12Z"),
            content = "Full content...",
        ),
        Article(
            source = Source(null, "Expansion.com"),
            author = "Stephen Foley",
            title = "PwC se 'acerca' a las criptomonedas tras el apoyo de Donald Trump",
            description = "PwC decidió acercarse al sector...",
            url = "https://www.expansion.com/...",
            urlToImage = "https://e01-phantom-expansion.uecdn.es/b19c9c38520ea908449ffe88d0ef065e/crop/0x0/2048x1365/resize/1200/f/webp/assets/multimedia/imagenes/2026/01/07/17678032249553.jpg",
            publishedAt = Instant.parse("2026-01-07T16:28:03Z"),
            content = "Full content...",
        ),
        Article(
            source = Source(null, "Cointelegraph"),
            author = "Aaron Wood",
            title = "La SEC ya es plenamente republicana y se prepara para legislar a favor de las criptomonedas",
            description = "La SEC tiene previsto continuar...",
            url = "https://es.cointelegraph.com/...",
            urlToImage = "https://images.cointelegraph.com/cdn-cgi/image/f=auto,onerror=redirect,w=1200/https://s3.cointelegraph.com/uploads/2026-01/019b98c3-eecd-7c02-928d-03c97e75a6a7.jpg",
            publishedAt = Instant.parse("2026-01-07T16:21:13Z"),
            content = "Full content...",
        ),
    )

    // --- System Bar Logic ---
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            val topBarBrush = SDKTheme.brushes.screenTopBar
            val glowBrush = SDKTheme.brushes.iconGlow

            TopAppBar(
                title = {
                    Box(contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(glowBrush),
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_crypto),
                            contentDescription = "Kotox Crypto Logo",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(32.dp),
                        )
                    }
                },
                modifier = Modifier.background(topBarBrush),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Box(contentAlignment = Alignment.Center) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(glowBrush),
                            )
                            Icon(Icons.Default.Language, "Language")
                        }
                    }
                    IconButton(onClick = { /* TODO */ }) {
                        Box(contentAlignment = Alignment.Center) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(glowBrush),
                            )
                            Icon(Icons.AutoMirrored.Outlined.Sort, "Sort")
                        }
                    }
                },
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
                    contentPadding = PaddingValues(
                        top = 8.dp,
                        bottom = 16.dp + contentPadding.calculateBottomPadding(),
                    ),
                ) {
                    items(items = articles, key = { it.title.hashCode() }) { article ->
                        ArticleListItem(
                            article = article,
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
private fun ArticlesContentScreenPreview() {
    SDKCryptoSampleAppTheme {
        ArticlesContentScreen(
            onItemClick = {},
            contentPadding = PaddingValues(bottom = 80.dp),
        )
    }
}
