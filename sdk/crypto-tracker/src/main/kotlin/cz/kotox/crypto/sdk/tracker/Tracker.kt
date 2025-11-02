package cz.kotox.crypto.sdk.tracker

import cz.kotox.crypto.sdk.common.Either
import cz.kotox.crypto.sdk.common.error.SdkError
import cz.kotox.crypto.sdk.tracker.domain.model.TradingProduct

/**
 * Tracker tbd
 */
public interface Tracker {

    public class Builder : TrackerBuilder()

    /**
     * Trading products
     */
    public suspend fun getTradingProducts(): Either<SdkError, List<TradingProduct>>
}
