# Crypto News & Sentiment Aggregator

## SDK 3: The News & Sentiment SDK 
This SDK handles all qualitative data, answering the "why" behind market movements.


## NewsAPI
https://newsapi.org/
NewsAPI is a simple and powerful API that aggregates headlines from tens of thousands of top news sources and blogs worldwide. 
It's not crypto-specific, but you can use it to search for articles about any cryptocurrency with excellent results

-> free tier 100/requests per day
-> in minutes delayed news
-> urlToImage feature: The API response often includes a urlToImage, making it easy to create a visually appealing news feed in your app.


##  CryptoPanic
https://cryptopanic.com/
-> pretty unuseful free tier (100 req/month, 24hours delayed news)
-> might be good fit with paid plan (this would required some business model)

API: CryptoPanic REST API
Purpose: To fetch news and sentiment data.
Core Function: getNews(forCoinSymbol: String)
This SDK's purpose is to fetch the latest news articles related to specific cryptocurrencies, helping users understand the context behind price movements.

User Action: "The price of Bitcoin is suddenly dropping. What's the news? Is the sentiment negative?"

The SDK's Job: To provide a function like getNews(forCoin: "BTC") that returns a list of current news articles.

The API for the Third SDK: CryptoPanic API
This is a very popular news aggregator in the crypto space, and its API is perfect for this use case.
