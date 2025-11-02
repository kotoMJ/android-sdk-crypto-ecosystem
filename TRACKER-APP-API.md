# App API (SDK) calls

## 1) Top 100 coins by market (Coindata SDK)

User choose from top 100 by market cap which crypto currency is user interested in.
https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1
Let's say user will choose **btc**.

## 2) Find the Live Product ID (Tracker SDK)

**Tracker SDK** needs to find the specific product to track on Coinbase.

_Action_: The app takes the symbol **btc** and searches the **cached list** it got from GET https://api.exchange.coinbase.com/products.
_Logic_: It finds the product where base_currency is "BTC" and quote_currency is "USD".
_Result_: It finds the product_id "BTC-USD".

We need to use persistence first here!
Dump the list of all products and search for desired one.

SELECT * FROM products WHERE base_currency = "BTC"
(Note: The base_currency field from Coinbase is likely uppercase "BTC", so make sure your query is case-insensitive or matches the case.)

## 3) Subscribe to the Live Price (Tracker SDK)

Now that it has the product_id, the tracker SDK opens the WebSocket to get the live price.
API Call: **wss://ws-feed.exchange.coinbase.com**
_Action_: The SDK opens the connection and sends the subscription message:

`{
"type": "subscribe",
"product_ids": ["BTC-USD"],
"channels": ["ticker"]
}`

_Result_: The server starts pushing live price updates for "BTC-USD" to your app.

## 4) Get Rich Coin Details (Coindata SDK)

Simultaneously, your coindata SDK fetches the logo, market cap, and description.
Internal Step (Mapping): The SDK's internal logic maps the symbol btc to the CoinGecko ID bitcoin (using the cached /coins/list data).
API Call:
GET https://api.coingecko.com/api/v3/coins/${CoinMarket.id}
e.g:
GET https://api.coingecko.com/api/v3/coins/bitcoin

_Result_: The SDK gets a large JSON object with all of Bitcoin's details.

## 5) Get Related News (News SDK)
The name field from your object ("Bitcoin") is the perfect string to use as the query parameter for the NewsAPI.

API Call:
https://newsapi.org/v2/everything?q=Bitcoin&apiKey=...



## Summary

In short: the user taps "Bitcoin," and the app immediately kicks off three separate jobs:

1) Tracker SDK ➡️ Connects to the WebSocket for the live price.
2) CoinData SDK ➡️ Calls CoinGecko for the logo and description.
3) News SDK ➡️ Calls NewsAPI for the latest articles.

All this data then populates the detail screen at the same time.