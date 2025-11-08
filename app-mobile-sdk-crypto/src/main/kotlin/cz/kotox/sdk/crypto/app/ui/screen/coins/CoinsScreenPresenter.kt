package cz.kotox.sdk.crypto.app.ui.screen.coins

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Suppress("FunctionName")
internal fun StoriesScreenPresenter(
    fakeFlow: Flow<String>,
    fakeFlow2: Flow<String>,
) = combine(
    flow = fakeFlow,
    flow2 = fakeFlow2,
) { fake, fake2 ->
    CoinsScreenState(
        fake = fake,
    )
}
