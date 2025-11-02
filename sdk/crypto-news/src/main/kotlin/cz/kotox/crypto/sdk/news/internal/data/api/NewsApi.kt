package cz.kotox.crypto.sdk.news.internal.data.api

import cz.kotox.crypto.sdk.news.internal.dto.NewsApiResponseDTO
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

public interface NewsApi {

    @GET("v2/everything")
    public suspend fun getNews(
        @Query("q") query: String,
        @Query("sortBy") sortBy: NewsApiSortBy,
        @Query("apiKey") apiKey: String,
    ): NewsApiResponseDTO
}

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
