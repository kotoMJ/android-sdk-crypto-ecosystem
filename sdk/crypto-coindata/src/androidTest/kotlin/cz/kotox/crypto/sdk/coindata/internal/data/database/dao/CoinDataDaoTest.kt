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

    // --- Tests ---

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

    @Test
    fun insertCoinDetail_savesRelationsCorrectly() = runBlocking {
        // GIVEN: A coin detail and some dynamic currency values (prices)
        val coinId = "bitcoin"
        val detail = createDetailEntity(coinId)
        val values = listOf(
            CoinDetailCurrencyValueEntity(coinId, "current_price", "usd", BigDecimal(50000.0)),
            CoinDetailCurrencyValueEntity(coinId, "current_price", "eur", BigDecimal(45000.0)),
            CoinDetailCurrencyValueEntity(coinId, "ath", "usd", BigDecimal(69000.0)),
        )

        // WHEN: We insert using the transaction
        coinDataDao.insertCoinDetailWithRelations(detail, values)

        // THEN: We can retrieve the coin and its specific relations
        val result = coinDataDao.getCoinDetail(coinId)
        assertNotNull(result)

        // Check Entity
        assertEquals("Bitcoin", result!!.coin.name)

        // Check Relations
        assertEquals(3, result.currencyValues.size)

        // Verify we can find the specific price we inserted
        val usdPrice = result.currencyValues.find {
            it.valueType == "current_price" && it.currency == "usd"
        }
        assertEquals("50000.0", usdPrice?.value)
    }

    @Test
    fun insertCoinDetail_cleansUpOldRelations() = runBlocking {
        // GIVEN: Existing data with old prices
        val coinId = "bitcoin"
        val detail = createDetailEntity(coinId)
        val oldValues = listOf(
            CoinDetailCurrencyValueEntity(coinId, "current_price", "usd", BigDecimal(100.0)), // Old price
        )
        coinDataDao.insertCoinDetailWithRelations(detail, oldValues)

        // WHEN: We insert new data for the same coin
        val newValues = listOf(
            CoinDetailCurrencyValueEntity(coinId, "current_price", "usd", BigDecimal(200.0)), // New price
        )
        coinDataDao.insertCoinDetailWithRelations(detail, newValues)

        // THEN: The DB should contain ONLY the new price (old one deleted)
        val result = coinDataDao.getCoinDetail(coinId)
        assertEquals(1, result!!.currencyValues.size)
        assertEquals("200.0", result.currencyValues.first().value)
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
