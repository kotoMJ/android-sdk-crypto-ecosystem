package cz.kotox.crypto.sdk.coindata.internal.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coin_detail_static")
internal data class CoinDetailStaticEntity(
    @PrimaryKey val id: String,
    val descriptionEn: String?, // From description.en
    val genesisDate: String?, // From genesis_date
    val homepageUrl: String?, // From links.homepage[0]
    val githubUrl: String?, // From links.repos_url.github[0]
    val lastFetched: Long,
)
