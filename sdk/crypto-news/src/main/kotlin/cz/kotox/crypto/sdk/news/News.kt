package cz.kotox.crypto.sdk.news

public interface News {

    public class Builder(newsServiceApiKey: String) : NewsBuilder(newsServiceApiKey = newsServiceApiKey)

//    public suspend fun getCoinMarkets(): Either<SdkError, List<CoinMarket>>
}
