package cz.kotox.crypto.sdk.internal.common

import android.annotation.SuppressLint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

public interface CoroutineDispatchers {
    public val fetchDispatcher: CoroutineDispatcher
    public val databaseDispatcher: CoroutineDispatcher
}

@SuppressLint("RawDispatchersUse")
public object SdkDispatchers : CoroutineDispatchers {
    override val fetchDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO

    override val databaseDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO
}
