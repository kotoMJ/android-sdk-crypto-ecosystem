package cz.kotox.crypto.sdk.tracker.internal.mapper

import android.icu.math.BigDecimal // Using your chosen type
import cz.kotox.crypto.sdk.internal.common.util.number.toBigDecimalOrNull
import cz.kotox.crypto.sdk.tracker.domain.model.TradingProduct
import cz.kotox.crypto.sdk.tracker.internal.dto.TradingProductDTO // Make sure this is the correct path to your DTO

public fun TradingProductDTO.toDomain(): TradingProduct {
    return TradingProduct(
        id = this.id,
        baseCurrency = this.baseCurrency,
        quoteCurrency = this.quoteCurrency,
        baseMinSize = this.baseMinSize.toBigDecimalOrNull(),
        baseMaxSize = this.baseMaxSize.toBigDecimalOrNull(),
        quoteIncrement = BigDecimal(this.quoteIncrement),
        baseIncrement = BigDecimal(this.baseIncrement),
        displayName = this.displayName,
        minMarketFunds = BigDecimal(this.minMarketFunds),
        maxMarketFunds = this.maxMarketFunds.toBigDecimalOrNull(),
        marginEnabled = this.marginEnabled,
        postOnly = this.postOnly,
        limitOnly = this.limitOnly,
        cancelOnly = this.cancelOnly,
        tradingDisabled = this.tradingDisabled,
        status = this.status,
        statusMessage = this.statusMessage,
        fxStablecoin = this.fxStablecoin,
        maxSlippagePercentage = this.maxSlippagePercentage.toBigDecimalOrNull(),
        auctionMode = this.auctionMode,
    )
}
