package cz.kotox.crypto.sdk.coindata

import android.content.Context

object TestCoinData {
    fun provideCoinData(context: Context): CoinData {
        return CoinData.Builder(context = context).build()
    }
}
