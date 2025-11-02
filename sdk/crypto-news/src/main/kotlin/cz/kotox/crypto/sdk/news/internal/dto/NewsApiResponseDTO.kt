package cz.kotox.crypto.sdk.news.internal.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the top-level response from the NewsAPI v2 /everything endpoint.
 */
@Serializable
public data class NewsApiResponseDTO(
    @SerialName("status")
    val status: String, // "ok" or "error"

    // --- Fields for a successful response ---

    @SerialName("totalResults")
    val totalResults: Int? = null,

    @SerialName("articles")
    val articles: List<ArticleDTO>? = null,

    // --- Fields for an error response ---

    @SerialName("code")
    val code: String? = null, // e.g., "apiKeyMissing"

    @SerialName("message")
    val message: String? = null,
)

/**
 * Represents a single news article.
 * This is the main item in the list, analogous to your TradingProductDTO.
 */
@Serializable
public data class ArticleDTO(
    @SerialName("source")
    val source: SourceDTO,

    @SerialName("author")
    val author: String? = null,

    @SerialName("title")
    val title: String,

    @SerialName("description")
    val description: String? = null,

    @SerialName("url")
    val url: String,

    @SerialName("urlToImage")
    val urlToImage: String? = null,

    @SerialName("publishedAt")
    val publishedAt: String, // ISO 8601 date-time string (e.g., "2025-10-24T06:30:00Z")

    @SerialName("content")
    val content: String? = null, // Note: This is often a snippet
)

/**
 * Represents the source of the news article, nested within [ArticleDTO].
 */
@Serializable
public data class SourceDTO(
    @SerialName("id")
    val id: String? = null, // e.g., "cnn" (can be null)

    @SerialName("name")
    val name: String, // e.g., "CNN"
)
