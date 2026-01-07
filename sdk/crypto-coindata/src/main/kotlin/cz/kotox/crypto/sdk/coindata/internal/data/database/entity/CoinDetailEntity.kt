package cz.kotox.crypto.sdk.coindata.internal.data.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.math.BigDecimal
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Entity(tableName = "coin_details")
internal data class CoinDetailEntity(
    @PrimaryKey
    val id: String,
    val symbol: String,
    val name: String,
    val assetPlatformId: String?,
    // Simple lists can be comma-separated via TypeConverter if strictly no JSON,
    // or we assume limited columns. For categories we'll use a delimiter.
    val categories: String, // Delimited string (e.g., "Defi|Meme")
    val genesisDate: String?,
    val marketCapRank: Int?,
    val coingeckoRank: Int?,
    val lastUpdated: Instant?,
    val cachedAt: Instant,

    @Embedded(prefix = "img_")
    val image: ImageEntityData,

    @Embedded(prefix = "desc_")
    val description: LocalizationEntityData,

    // Flattened simple fields from MarketData
    val circulatingSupply: BigDecimal,
    val totalSupply: BigDecimal?,
    val maxSupply: BigDecimal?,
)

// Helper data classes for Embedding
internal data class ImageEntityData(
    val thumb: String,
    val small: String,
    val large: String,
)

internal data class LocalizationEntityData(
    val en: String?,
    val de: String?,
    val es: String?,
    val fr: String?,
)

// --- 3. Normalized Table for Market Data Maps ---
// Stores: current_price, ath, market_cap, etc.
@Entity(
    tableName = "coin_detail_currency_values",
    primaryKeys = ["coinId", "valueType", "currency"],
    foreignKeys = [
        ForeignKey(
            entity = CoinDetailEntity::class,
            parentColumns = ["id"],
            childColumns = ["coinId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("coinId")],
)
internal data class CoinDetailCurrencyValueEntity(
    val coinId: String,
    val valueType: String, // e.g. "current_price", "ath", "total_volume"
    val currency: String, // e.g. "usd", "eur"
    val value: BigDecimal,
)

// --- 4. Aggregate Result for Room Query ---
internal data class CoinDetailWithRelations(
    @Embedded val coin: CoinDetailEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "coinId",
    )
    val currencyValues: List<CoinDetailCurrencyValueEntity>,
)
