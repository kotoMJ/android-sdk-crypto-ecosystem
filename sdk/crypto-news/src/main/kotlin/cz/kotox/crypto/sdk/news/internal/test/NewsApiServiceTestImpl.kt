package cz.kotox.crypto.sdk.news.internal.test

import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.error.SdkError
import cz.kotox.crypto.sdk.news.domain.NewsRequestContext
import cz.kotox.crypto.sdk.news.internal.dto.NewsApiResponseDTO

internal class NewsApiServiceTestImpl(
    private val newsRequestContext: NewsRequestContext,
) {
    suspend fun getNews(
        playIntegrityToken: String,
    ): Either<SdkError, NewsApiResponseDTO> = newsRequestContext.withApi {
        getNews(
            integrityToken = playIntegrityToken,
        )
    }
}
