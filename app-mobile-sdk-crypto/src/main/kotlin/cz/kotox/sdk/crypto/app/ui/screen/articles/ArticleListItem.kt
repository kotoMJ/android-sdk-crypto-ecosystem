package cz.kotox.sdk.crypto.app.ui.screen.articles

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import cz.kotox.crypto.sdk.news.domain.Article
import cz.kotox.crypto.sdk.news.domain.Source
import cz.kotox.sdk.crypto.app.R
import cz.kotox.sdk.crypto.app.ui.theme.SDKCryptoSampleAppTheme
import cz.kotox.sdk.crypto.app.utils.formatter.formatDateArticle
import kotlin.time.Clock

@Composable
fun ArticleListItem(
    article: Article,
    onItemClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        onClick = { onItemClick(article) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            // 1. Thumbnail Image
            AsyncImage(
                model = article.urlToImage,
                placeholder = painterResource(id = R.drawable.ic_crypto), // Use generic crypto icon as placeholder
                error = painterResource(id = R.drawable.ic_crypto),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 2. Text Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                // Formatter logic (inline or extracted)
                val dateStr = remember(article.publishedAt) {
                    article.publishedAt.formatDateArticle()
                }

                // Metadata Row (Source • Date)
                Text(
                    text = "${article.source.name} • $dateStr",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary, // Gold color
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                // Title
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun ArticleListItemPreview() {
    SDKCryptoSampleAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ArticleListItem(
                article = Article(
                    source = Source(id = "cointelegraph", name = "Cointelegraph"),
                    author = "Author Name",
                    title = "Bitcoin faces selling pressure as resistance holds at \$95k",
                    description = "Description...",
                    url = "https://google.com",
                    urlToImage = null,
                    publishedAt = Clock.System.now(),
                    content = "Full content...",
                ),
                onItemClick = {},
            )
        }
    }
}
