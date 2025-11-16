package cz.kotox.sdk.crypto.app.navigation

import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope

/**
 * A SceneStrategy that tries a list of other strategies in order.
 * The first strategy to return a non-null Scene "wins".
 */
class CompositeSceneStrategy<T : Any>(
    private val strategies: List<SceneStrategy<T>>,
) : SceneStrategy<T> {

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        // Loop through all our strategies in the order they were given
        for (strategy in strategies) {
            // Ask the current strategy if it can handle this scene
            val scene = with(strategy) { calculateScene(entries) }

            // If it returned a scene (not null), use it!
            if (scene != null) {
                return scene
            }
        }

        // If no custom strategy handled it, return null.
        // This lets NavDisplay use its default full-screen strategy.
        return null
    }
}
