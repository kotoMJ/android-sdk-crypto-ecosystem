# Tracker SDK

Abstracts real-time crypto tracker as the coinbase websocket feed.

## Plan

**API 2 (Rich Context): CoinGecko REST API**
This is a comprehensive REST API that provides all the static information about thousands of cryptocurrencies. 
It solves the "too simple" and "hard to link" problems of the logo API.

- API Provider: CoinGecko
- Why it's a great fit:
  - **Direct, Foolproof Linking:** The Coinbase feed gives you a product_id like "BTC-USD". You can easily parse the base currency ("BTC") and use it to query CoinGecko. The CoinGecko API uses standard IDs like "bitcoin", which are easily mapped from the symbols.
  - **Extremely Rich Data:** You get far more than just a logo. You can fetch the full name, description, market data (market cap, 24h change %), links, and multiple image sizes.
  - **Generous Free Tier:** The public API is free to use and well-documented.
- **How it connects:** You get a live price for "BTC-USD" from the WebSocket. Your SDK takes the "BTC" part, maps it to the CoinGecko ID "bitcoin", and makes a simple GET request.

**Example call (get details for Bitcoin):**

https://api.coingecko.com/api/v3/coins/bitcoin
This returns a rich JSON payload with everything you need to build a beautiful UI, including an image object with small, thumb, and large logo URLs.

## SDK 2: The Data & Utility SDK (wrapping CoinGecko)
This is your main data provider SDK. It answers all quantitative questions about the assets.

API: CoinGecko REST API

Purpose: To provide detailed information and perform calculations.

Core Functions:

getCoinDetails(id: String): Fetches rich context (logo, market cap, etc.) from the /coins/{id} endpoint.

getConversionRate(fromId: String, toId: String): Fetches the exchange rate from the /simple/price endpoint. This is where the calculator logic lives.

getCoinList(): Fetches the complete list of coins to map symbols to IDs.