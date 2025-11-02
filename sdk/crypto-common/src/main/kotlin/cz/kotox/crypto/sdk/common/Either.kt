package cz.kotox.crypto.sdk.common

import kotlinx.coroutines.CancellationException

public sealed class Either<out E, out V> {
    public data class Value<out V>(val value: V) : Either<Nothing, V>()

    public data class Error<out E>(val error: E) : Either<E, Nothing>()
}

@Suppress("TooGenericExceptionCaught")
public suspend fun <V> either(action: suspend () -> V): Either<Throwable, V> =
    try {
        Either.Value(action())
    } catch (e: CancellationException) {
        // Coroutine cancelled, don't block it
        throw e
    } catch (e: Exception) {
        Either.Error(e)
    }

public inline infix fun <V, V2, E> Either<E, V>.map(func: (V) -> V2): Either<E, V2> =
    when (this) {
        is Either.Value -> Either.Value(func(value))
        is Either.Error -> this
    }

public class MappingException(cause: Exception) : Exception(cause)

@Suppress("TooGenericExceptionCaught")
public inline fun <V, V2> V.mapWithMappingException(mapper: (V) -> V2): V2 {
    try {
        return mapper(this)
    } catch (e: Exception) {
        throw MappingException(e)
    }
}

@Suppress("TooGenericExceptionCaught")
public inline fun <V, V2> List<V>.mapWithMappingException(mapper: (V) -> V2): List<V2> {
    try {
        return this.map { mapper(it) }
    } catch (e: Exception) {
        throw MappingException(e)
    }
}

public inline infix fun <V, E, E2> Either<E, V>.mapError(func: (E) -> E2): Either<E2, V> =
    when (this) {
        is Either.Value -> this
        is Either.Error -> Either.Error(func(error))
    }

public inline infix fun <V, V2, E> Either<E, List<V>>.mapIterable(func: (V) -> V2): Either<E, List<V2>> =
    when (this) {
        is Either.Value -> Either.Value(value.map(func))
        is Either.Error -> this
    }

public inline infix fun <V, V2, E> Either<E, V>.flatMap(func: (V) -> Either<E, V2>): Either<E, V2> =
    when (this) {
        is Either.Value -> func(value)
        is Either.Error -> this
    }

public inline infix fun <V, E, E2> Either<E, V>.flatMapError(func: (E) -> Either<E2, V>): Either<E2, V> =
    when (this) {
        is Either.Value -> this
        is Either.Error -> func(error)
    }

public inline fun <V, E> Either<E, V>.doOnValue(action: (V) -> Unit): Either<E, V> =
    also {
        if (this is Either.Value) {
            action(this.value)
        }
    }

public inline fun <V, E> Either<E, V>.doOnError(action: (E) -> Unit): Either<E, V> =
    also {
        if (this is Either.Error) {
            action(this.error)
        }
    }

public inline fun <E, V> Either<E, V>.catchError(action: (E) -> Nothing): V =
    when (this) {
        is Either.Error -> action(this.error)
        is Either.Value -> this.value
    }

public inline fun <E, V, A> Either<E, V>.fold(
    error: (E) -> A,
    value: (V) -> A,
): A =
    when (this) {
        is Either.Value -> value(this.value)
        is Either.Error -> error(this.error)
    }

public inline fun <E> Either<E, Unit>.foldUnit(error: (E) -> Unit): Unit = fold(error = error, value = {})

public fun <E, V> Either<E, V>.getValue(): V? =
    when (this) {
        is Either.Value -> this.value
        is Either.Error -> null
    }

public fun <E, V> Either<E, V>.getError(): E? =
    when (this) {
        is Either.Value -> null
        is Either.Error -> this.error
    }
