package cz.kotox.crypto.sdk.news.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.Instant

/**
 * Represents a single news article.
 * This is the domain model, ready to be used in the UI/domain layers.
 */
@Parcelize
public data class Article(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: Instant, // Changed from String to Instant
    val content: String?,
) : Parcelable

/**
 * Represents the source of a news article.
 */
@Parcelize
public data class Source(
    val id: String?,
    val name: String,
) : Parcelable
