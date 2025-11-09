package cz.kotox.sdk.crypto.app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.compose.rememberNavController
import cz.kotox.sdk.crypto.app.navigation.MainNavHost
import cz.kotox.sdk.crypto.app.ui.component.Screen
import timber.log.Timber

@Composable
fun MainActivityContent(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            Timber.d("New destination: $destination")
        }
    }

    Screen(
        modifier = modifier
            .fillMaxSize()
            .testTag("main_activity"),
    ) {
        MainNavHost(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            navController = navController,
        )

//        MainBottomNavigation(
//            modifier = Modifier
//                .fillMaxWidth()
//                .testTag("bottom_navigation"),
//            navController = navController,
//        )
    }
}
