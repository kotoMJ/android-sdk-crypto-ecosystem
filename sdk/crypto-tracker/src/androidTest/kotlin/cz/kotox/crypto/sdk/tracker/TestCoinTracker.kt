package cz.kotox.crypto.sdk.tracker

object TestCoinTracker {
    fun provideTracker(): Tracker {
        return Tracker.Builder().build()
    }
}
