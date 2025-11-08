package cz.kotox.sdk.crypto.app.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import cz.kotox.sdk.crypto.app.R

sealed interface BottomNavScreen {
    val route: String

    @get:DrawableRes
    val iconId: Int

    @get:StringRes
    val titleId: Int

    data object Coins : BottomNavScreen {
        override val route: String = ROUTE_COINS
        override val iconId = R.drawable.ic_coins
        override val titleId = R.string.bottom_screen_title_coins
    }

    companion object {
        val values = listOf(
            Coins,
        )
    }
}
