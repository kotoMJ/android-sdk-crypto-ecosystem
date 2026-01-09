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
import cz.kotox.sdk.crypto.app.R
import cz.kotox.sdk.crypto.app.ui.mock.articles.articles
import cz.kotox.sdk.crypto.app.ui.theme.SDKCryptoSampleAppTheme
import cz.kotox.sdk.crypto.app.ui.theme.SDKTheme

@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticlesContentScreen(
    onItemClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
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
                    items(items = articles, key = { it.url.hashCode() + it.publishedAt.hashCode() }) { article ->
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
