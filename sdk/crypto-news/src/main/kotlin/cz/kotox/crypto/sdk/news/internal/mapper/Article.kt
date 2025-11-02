package cz.kotox.crypto.sdk.news.internal.mapper

import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.error.ApiError
import cz.kotox.crypto.sdk.common.error.SdkError
import cz.kotox.crypto.sdk.news.domain.Article
import cz.kotox.crypto.sdk.news.domain.Source
import cz.kotox.crypto.sdk.news.internal.dto.ArticleDTO
import cz.kotox.crypto.sdk.news.internal.dto.NewsApiResponseDTO
import cz.kotox.crypto.sdk.news.internal.dto.SourceDTO
import cz.kotox.crypto.sdk.news.internal.utils.logE
import java.time.Instant
import java.time.format.DateTimeParseException

/**
 * Maps the entire [NewsApiResponseDTO] to a list of domain [Article] models.
 * It filters out unsuccessful responses and any articles that fail to parse.
 */
public fun NewsApiResponseDTO.toDomainArticleList(): Either<SdkError, List<Article>> {
    // Only map successful responses that contain an article list
    return if (this.status != "ok" || this.articles == null) {
        Either.Error(ApiError.ResponseError(this.message ?: ""))
    } else {
        Either.Value(this.articles.mapNotNull { it.toDomain() })
    }
}

/**
 * Maps a single [ArticleDTO] to the domain [Article] model.
 *
 * Returns null if essential data (like 'publishedAt') is invalid,
 * allowing the caller (e.g., `mapNotNull`) to filter it out.
 */
private fun ArticleDTO.toDomain(): Article? {
    val publishedAtInstant: Instant = try {
        Instant.parse(this.publishedAt)
    } catch (e: DateTimeParseException) {
        // Log the error: the API returned an unparseable date
        logE(e) { "Unable to parse publishedAt of article: $this" }
        return null
    }

    return Article(
        source = this.source.toDomain(), // Recursively map the nested object
        author = this.author,
        title = this.title,
        description = this.description,
        url = this.url,
        urlToImage = this.urlToImage,
        publishedAt = publishedAtInstant,
        content = this.content,
    )
}

/**
 * Maps a [SourceDTO] to the domain [Source] model.
 */
private fun SourceDTO.toDomain(): Source {
    return Source(
        id = this.id,
        name = this.name,
    )
}
