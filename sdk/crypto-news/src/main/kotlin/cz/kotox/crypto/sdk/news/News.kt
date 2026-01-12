package cz.kotox.crypto.sdk.news

import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.domain.model.integrity.SdkIntegrityToken
import cz.kotox.crypto.sdk.common.error.SdkError
import cz.kotox.crypto.sdk.news.internal.dto.NewsApiResponseDTO

public interface News {

    public class Builder : NewsBuilder()

    public fun getNews(integrityToken: SdkIntegrityToken): Either<SdkError, NewsApiResponseDTO>
}
