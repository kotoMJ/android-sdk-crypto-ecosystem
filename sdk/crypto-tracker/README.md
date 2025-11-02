# Tracker SDK

Abstracts real-time crypto tracker as the coinbase websocket feed.

## Plan

API 1 (Live Action): Coinbase WebSocket Feed
This is a public WebSocket that streams real-time market data. It's exactly what you need to show your SDK handling persistent, push-based connections.

 - API Provider: Coinbase
 - Endpoint URL: wss://ws-feed.exchange.coinbase.com
 - Why it's perfect: 
   - True Real-Time Data: Instead of polling, the server pushes updates to your app the instant they happen. This is perfect for demonstrating live UI changes.
   - Technically Impressive: Managing a WebSocket lifecycle (connecting, subscribing, handling messages, reconnecting on failure) is a non-trivial task and a great showcase for a robust SDK. 
   - No API Key Needed: The public feed is free and requires no authentication.

How it works:
 - Your SDK opens a connection to the WebSocket URL.
 - It sends a JSON message to subscribe to one or more product "channels."
 - The server then starts streaming ticker messages with live price updates.


**Example subscription message (sent from your SDK):**

`{
"type": "subscribe",
"product_ids": [
"BTC-USD",
"ETH-USD",
"SOL-USD"
],
"channels": ["ticker"]
}`

**Example message received (pushed from the server):**

`{
"type": "ticker",
"sequence": 36983844356,
"product_id": "BTC-USD",
"price": "68324.21",
"open_24h": "67998.41",
"volume_24h": "24333.322",
"side": "buy",
"time": "2025-10-15T15:55:23.450284Z"
}`

## SDK 1: The Live Feed SDK (wrapping Coinbase)
This SDK has one focused, high-performance job: managing the real-time WebSocket connection.

API: Coinbase WebSocket Feed

Purpose: To stream live price ticks.

Core Function: subscribeToTicker(productIds: List<String>, onUpdate: (Ticker) -> Unit)