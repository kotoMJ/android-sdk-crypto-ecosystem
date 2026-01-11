package cz.kotox.crypto.sdk.news.internal.data.api

import cz.kotox.crypto.sdk.news.internal.dto.NewsApiResponseDTO
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import kotlinx.serialization.Serializable

public interface NewsApi {

    @Headers("Content-Type: application/json")
    @POST("api/news")
    public suspend fun fetchNews(
        @Body request: NewsFetchRequest,

    ): NewsApiResponseDTO
}

@Serializable
public data class NewsFetchRequest(
    val integrityToken: String,
)

public enum class NewsApiSortBy(
    public val value: String,
) {

    /**
     * Articles more closely related to the query (q) come first.
     */
    RELEVANCY("relevancy"),

    /**
     * Articles from popular sources and publishers come first.
     */
    POPULARITY("popularity"),

    /**
     * Newest articles come first. This is the default.
     */
    PUBLISHED_AT("publishedAt"),
    ;

    /**
     * This override is the key.
     * Ktorfit's @Query will call this method to get the
     * string value for the URL.
     */
    override fun toString(): String {
        return this.value
    }
}
