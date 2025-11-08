package cz.kotox.sdk.crypto.app.extension

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

fun <T> DataStore<Preferences>.getBlocking(key: Preferences.Key<T>, default: T): T {
    return runBlocking { getFlow(key, default).first() }
}

fun <T> DataStore<Preferences>.getBlockingOrNull(key: Preferences.Key<T>): T? {
    return runBlocking { getOrNullFlow(key).first() }
}

fun <T> DataStore<Preferences>.getFlow(key: Preferences.Key<T>, default: T): Flow<T> {
    return data.map { preferences ->
        preferences[key] ?: default
    }
}

fun <T> DataStore<Preferences>.getOrNullFlow(key: Preferences.Key<T>): Flow<T?> {
    return data.map { preferences ->
        preferences[key]
    }
}

fun <T> MutablePreferences.update(key: Preferences.Key<T>, value: T?) {
    if (value != null) {
        this[key] = value
    } else {
        remove(key)
    }
}

suspend inline fun <T> DataStore<Preferences>.updateValue(
    key: Preferences.Key<T>,
    crossinline value: (T?) -> T?,
) {
    edit { preferences ->
        val currentValue = preferences[key]
        preferences.update(key, value(currentValue))
    }
}

suspend inline fun DataStore<Preferences>.clear() {
    edit { preferences ->
        preferences.clear()
    }
}
