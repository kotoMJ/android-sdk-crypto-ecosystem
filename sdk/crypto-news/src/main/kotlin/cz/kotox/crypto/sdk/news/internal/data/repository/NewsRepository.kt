package cz.kotox.crypto.sdk.news.internal.data.repository

import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.error.ApiError
import cz.kotox.crypto.sdk.common.error.IntegrityError
import cz.kotox.crypto.sdk.common.error.SdkError
import cz.kotox.crypto.sdk.common.fold
import cz.kotox.crypto.sdk.internal.integrity.Integrity
import cz.kotox.crypto.sdk.internal.integrity.domain.SdkIntegrityToken
import cz.kotox.crypto.sdk.news.domain.Article
import cz.kotox.crypto.sdk.news.domain.NewsRequestContext
import cz.kotox.crypto.sdk.news.internal.mapper.toDomainArticleList
import cz.kotox.crypto.sdk.news.internal.utils.logE

internal class NewsRepository(
    private val requestContext: NewsRequestContext,
    private val integrity: Integrity,
) {

    suspend fun getNews(): Either<SdkError, List<Article>> {
        val timestamp = System.currentTimeMillis()
        // "fetchNews|sortBy:${sortBy.value}|$timestamp"
        val bindingContent = "getNews|$timestamp"
        val requestHash = integrity.getIntegrityHash(bindingContent)

        val integrityToken: SdkIntegrityToken = integrity.getFreshToken(requestHash)
            ?: if (integrity.getSecurityHeader() == null) {
                return Either.Error(IntegrityError("Unable to get integrity token for hash $requestHash"))
            } else {
                SdkIntegrityToken("UsingSecurityHeader")
            }

        requestContext.withApi { getNews(integrityToken = integrityToken.value) }.fold(
            { newsError ->
                logE(newsError, { "Unable to refresh getNews: $newsError" })
                return if (newsError is ApiError.ForbiddenResponseError) {
                    // Forbidden from BFF means integrity was not verified!
                    Either.Error(IntegrityError(newsError.message, newsError.cause))
                } else {
                    Either.Error(newsError)
                }
            },
            { newsResponse ->
                return newsResponse.toDomainArticleList(requireTitle = true)
            },
        )
    }
}
