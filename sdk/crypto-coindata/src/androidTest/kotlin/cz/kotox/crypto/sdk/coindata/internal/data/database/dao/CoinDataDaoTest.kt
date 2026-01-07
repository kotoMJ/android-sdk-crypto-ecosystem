package cz.kotox.crypto.sdk.coindata.internal.data.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cz.kotox.crypto.sdk.coindata.internal.data.database.CoinDatabase
import cz.kotox.crypto.sdk.coindata.internal.data.database.entity.CoinDetailCurrencyValueEntity
import cz.kotox.crypto.sdk.coindata.internal.data.database.entity.CoinDetailEntity
import cz.kotox.crypto.sdk.coindata.internal.data.database.entity.CoinMarketEntity
import cz.kotox.crypto.sdk.coindata.internal.data.database.entity.ImageEntityData
import cz.kotox.crypto.sdk.coindata.internal.data.database.entity.LocalizationEntityData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.math.BigDecimal
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@RunWith(AndroidJUnit4::class)
class CoinDataDaoTest {

    private lateinit var db: CoinDatabase
    private lateinit var coinDataDao: CoinDataDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, CoinDatabase::class.java)
            .allowMainThreadQueries() // Allowed for testing
            .build()
        coinDataDao = db.coinDataDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertMarkets_retrievesCorrectCurrency() = runBlocking {
        // GIVEN: Markets for USD and EUR
        val btcUsd = createMarketEntity(id = "bitcoin", currency = "usd", rank = 1)
        val ethUsd = createMarketEntity(id = "ethereum", currency = "usd", rank = 2)
        val btcEur = createMarketEntity(id = "bitcoin", currency = "eur", rank = 1)

        coinDataDao.insertMarkets(listOf(btcUsd, ethUsd, btcEur))

        // WHEN: We query for USD
        val usdMarkets = coinDataDao.getMarkets("usd")

        // THEN: We get only USD markets, sorted by rank
        assertEquals(2, usdMarkets.size)
        assertEquals("bitcoin", usdMarkets[0].id)
        assertEquals("ethereum", usdMarkets[1].id)
        assertEquals("usd", usdMarkets[0].vsCurrency)
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun insertCoinDetail_savesRelationsCorrectly() = runBlocking {
        val coinId = "bitcoin"
        val staleMarket = createMarketEntity(coinId, "usd", 1).copy(
            currentPrice = BigDecimal("100.0"),
        )
        coinDataDao.insertMarkets(listOf(staleMarket))

        // GIVEN: A fresh Coin Detail with new prices (Price 50,000.0)
        val detail = createDetailEntity(coinId)
        val values = listOf(
            CoinDetailCurrencyValueEntity(coinId, "current_price", "usd", BigDecimal("50000.0")), // New Price
            CoinDetailCurrencyValueEntity(coinId, "current_price", "eur", BigDecimal("45000.0")),
            CoinDetailCurrencyValueEntity(coinId, "ath", "usd", BigDecimal("69000.0")),
        )

        // WHEN: We insert Detail (triggers Sync)
        coinDataDao.insertCoinDetailWithRelations(detail, values)

        // THEN 1: The Detail relations are saved
        val detailResult = coinDataDao.getCoinDetail(coinId)
        assertNotNull(detailResult)
        assertEquals(3, detailResult!!.currencyValues.size)

        // Find USD price in relations
        val usdPriceRelation = detailResult.currencyValues.find {
            it.valueType == "current_price" && it.currency == "usd"
        }
        // Use compareTo for BigDecimal safety
        assertTrue(BigDecimal("50000.0").compareTo(usdPriceRelation?.value) == 0)

        // THEN 2: The Market Entity is automatically synced
        val marketResult = coinDataDao.getMarkets("usd").first()
        assertEquals(
            "Market price should have been synced from Detail",
            0,
            BigDecimal("50000.0").compareTo(marketResult.currentPrice),
        )
    }

    @Test
    fun insertCoinDetail_cleansUpOldRelations() = runBlocking {
        // GIVEN: Existing data with old prices
        val coinId = "bitcoin"
        val detail = createDetailEntity(coinId)
        val oldValues = listOf(
            CoinDetailCurrencyValueEntity(coinId, "current_price", "usd", BigDecimal("100.0")),
        )
        coinDataDao.insertCoinDetailWithRelations(detail, oldValues)

        // WHEN: We insert new data for the same coin
        val newValues = listOf(
            CoinDetailCurrencyValueEntity(coinId, "current_price", "usd", BigDecimal("200.0")),
        )
        coinDataDao.insertCoinDetailWithRelations(detail, newValues)

        // THEN: The DB should contain ONLY the new price (old one deleted)
        val result = coinDataDao.getCoinDetail(coinId)
        assertEquals(1, result!!.currencyValues.size)

        val value = result.currencyValues.first().value
        assertTrue("Expected 200.0 but got $value", BigDecimal("200.0").compareTo(value) == 0)
    }

    @Test
    fun getMarketsFlow_emitsUpdates() = runBlocking {
        // GIVEN: Initial empty state
        val flow = coinDataDao.getMarketsFlow("usd")
        val initialEmission = flow.first()
        assertTrue(initialEmission.isEmpty())

        // WHEN: We insert data
        val btcUsd = createMarketEntity(id = "bitcoin", currency = "usd", rank = 1)
        coinDataDao.insertMarkets(listOf(btcUsd))

        // THEN: The flow should emit the new data
        // (Note: Testing Flow strictly usually requires Turbine, but for simple Room tests simply re-collecting or using proper test scope works.
        // Here we simulate a re-query behavior that UI would see)
        val newEmission = coinDataDao.getMarketsFlow("usd").first()
        assertEquals(1, newEmission.size)
        assertEquals("bitcoin", newEmission.first().id)
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun insertCoinDetail_syncsAllMarketFields() = runBlocking {
        // GIVEN: A stale Market entity
        // Initial state: Price = 100.0, Change = 5.0%
        val coinId = "bitcoin"
        val staleMarket = createMarketEntity(coinId, "usd", 1).copy(
            currentPrice = BigDecimal("100.0"),
            priceChangePercentage24h = BigDecimal("5.0"),
        )
        coinDataDao.insertMarkets(listOf(staleMarket))

        // GIVEN: Fresh Detail data with significantly different values
        // New state: Price = 50,000.0, Change = -10.5%
        val detail = createDetailEntity(coinId)
        val newValues = listOf(
            CoinDetailCurrencyValueEntity(coinId, "current_price", "usd", BigDecimal("50000.0")),
            CoinDetailCurrencyValueEntity(coinId, "price_change_pct_24h", "usd", BigDecimal("-10.5")),
        )

        // WHEN: We insert Detail (triggers Sync)
        coinDataDao.insertCoinDetailWithRelations(detail, newValues)

        // THEN: The Market entity should reflect BOTH updates
        val updatedMarket = coinDataDao.getMarkets("usd").first()

        // Verify Price Sync
        assertTrue(
            "Price should be synced to 50000.0",
            BigDecimal("50000.0").compareTo(updatedMarket.currentPrice) == 0,
        )

        // Verify Price Change Sync
        assertTrue(
            "Price Change % should be synced to -10.5",
            BigDecimal("-10.5").compareTo(updatedMarket.priceChangePercentage24h!!) == 0,
        )
    }

    // --- Helpers ---

    @OptIn(ExperimentalTime::class)
    private fun createMarketEntity(id: String, currency: String, rank: Int) = CoinMarketEntity(
        id = id,
        vsCurrency = currency,
        symbol = id.take(3),
        name = id.capitalize(),
        imageUrl = "http://image.com/$id",
        currentPrice = BigDecimal(100.0),
        marketCap = BigDecimal(1000),
        marketCapRank = rank,
        fullyDilutedValuation = null,
        totalVolume = BigDecimal(500.0),
        high24h = BigDecimal(105.0),
        low24h = BigDecimal(95.0),
        priceChange24h = BigDecimal(5.0),
        priceChangePercentage24h = BigDecimal(5.0),
        marketCapChange24h = BigDecimal(0.0),
        marketCapChangePercentage24h = BigDecimal(0.0),
        circulatingSupply = BigDecimal(100.0),
        totalSupply = null,
        maxSupply = null,
        ath = BigDecimal(200.0),
        athChangePercentage = BigDecimal(-50.0),
        athDate = Instant.parse("2021-01-01T00:00:00Z"),
        atl = BigDecimal(1.0),
        atlChangePercentage = BigDecimal(10000.0),
        atlDate = Instant.parse("2015-01-01T00:00:00Z"),
        roiTimes = null,
        roiCurrency = null,
        roiPercentage = null,
        lastUpdated = Clock.System.now(),
        cachedAt = Clock.System.now(),
    )

    @OptIn(ExperimentalTime::class)
    private fun createDetailEntity(id: String) = CoinDetailEntity(
        id = id,
        symbol = id.take(3),
        name = id.capitalize(),
        assetPlatformId = null,
        categories = "Test",
        genesisDate = null,
        marketCapRank = 1,
        coingeckoRank = 1,
        image = ImageEntityData("", "", ""),
        description = LocalizationEntityData("Desc", null, null, null),
        circulatingSupply = BigDecimal(1000),
        totalSupply = null,
        maxSupply = null,
        lastUpdated = Clock.System.now(),
        cachedAt = Clock.System.now(),
    )

    private fun String.capitalize() = replaceFirstChar { it.uppercase() }
}
