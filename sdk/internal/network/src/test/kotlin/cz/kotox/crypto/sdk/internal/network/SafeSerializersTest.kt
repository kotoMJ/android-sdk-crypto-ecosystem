package cz.kotox.crypto.sdk.internal.network

import cz.kotox.crypto.sdk.common.logger.LogPriority
import cz.kotox.crypto.sdk.common.logger.SDKLoggerCallback
import cz.kotox.crypto.sdk.internal.logger.SDKLogger
import cz.kotox.crypto.sdk.internal.logger.impl.AndroidLogger
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class SafeSerializersTest {

    private lateinit var fakeLogger: SDKLogger

    @Before
    fun setup() {
        fakeLogger = AndroidLogger(
            originalTag = "SafeSerializersTest",
            loggerCallback = object : SDKLoggerCallback {
                override fun onLogMessage(
                    tag: String,
                    priority: LogPriority,
                    t: Throwable?,
                    message: String,
                ) {
                    println(message = message)
                }
            },
        )
    }

// ============================================================================================
    // 1. SafeNullableLongSerializer Tests (Field is Long?)
    // ============================================================================================

    @Test
    fun `NullableSerializer - Happy Path - parses valid long (Relaxed)`() {
        val json = createTestJson(strictMode = false)
        val result = json.decodeFromString(SafeNullableLongSerializer(fakeLogger, false), "123456")
        assertEquals(123456L, result)
    }

    @Test
    fun `NullableSerializer - Happy Path - parses valid long (Strict)`() {
        // Strict mode should NOT throw for valid data
        val json = createTestJson(strictMode = true)
        val result = json.decodeFromString(SafeNullableLongSerializer(fakeLogger, true), "123456")
        assertEquals(123456L, result)
    }

    @Test
    fun `NullableSerializer - parses explicit null (Relaxed)`() {
        val json = createTestJson(strictMode = false)
        val result = json.decodeFromString(SafeNullableLongSerializer(fakeLogger, false), "null")
        assertNull(result)
    }

    @Test
    fun `NullableSerializer - parses explicit null (Strict)`() {
        // Strict mode should NOT throw for explicit null on a nullable field
        val json = createTestJson(strictMode = true)
        val result = json.decodeFromString(SafeNullableLongSerializer(fakeLogger, true), "null")
        assertNull(result)
    }

    @Test
    fun `NullableSerializer - Truncation (Relaxed) - parses double as long`() {
        val json = createTestJson(strictMode = false)
        val result = json.decodeFromString(SafeNullableLongSerializer(fakeLogger, false), "31.38")
        assertEquals(31L, result)
    }

    @Test
    fun `NullableSerializer - Truncation (Strict) - throws exception`() {
        val json = createTestJson(strictMode = true)
        val serializer = SafeNullableLongSerializer(fakeLogger, strictMode = true)
        assertThrows(SerializationException::class.java) {
            json.decodeFromString(serializer, "31.38")
        }
    }

    @Test
    fun `NullableSerializer - Garbage Data (Relaxed) - returns null`() {
        val json = createTestJson(strictMode = false)
        val result = json.decodeFromString(SafeNullableLongSerializer(fakeLogger, false), "\"invalid-string\"")
        assertNull(result)
    }

    @Test
    fun `NullableSerializer - Garbage Data (Strict) - throws exception`() {
        val json = createTestJson(strictMode = true)
        val serializer = SafeNullableLongSerializer(fakeLogger, strictMode = true)
        assertThrows(SerializationException::class.java) {
            json.decodeFromString(serializer, "\"invalid-string\"")
        }
    }

    // ============================================================================================
    // 2. SafeLongSerializer Tests (Field is Long - Non Null)
    // ============================================================================================

    @Test
    fun `NonNullSerializer - Happy Path - parses valid long (Relaxed)`() {
        val json = createTestJson(strictMode = false)
        val result = json.decodeFromString(SafeLongSerializer(fakeLogger, false), "999")
        assertEquals(999L, result)
    }

    @Test
    fun `NonNullSerializer - Happy Path - parses valid long (Strict)`() {
        val json = createTestJson(strictMode = true)
        val result = json.decodeFromString(SafeLongSerializer(fakeLogger, true), "999")
        assertEquals(999L, result)
    }

    @Test
    fun `NonNullSerializer - Truncation (Relaxed) - parses double as long`() {
        val json = createTestJson(strictMode = false)
        val result = json.decodeFromString(SafeLongSerializer(fakeLogger, false), "99.99")
        assertEquals(99L, result)
    }

    @Test
    fun `NonNullSerializer - Truncation (Strict) - throws exception`() {
        val json = createTestJson(strictMode = true)
        val serializer = SafeLongSerializer(fakeLogger, strictMode = true)
        assertThrows(SerializationException::class.java) {
            json.decodeFromString(serializer, "99.99")
        }
    }

    @Test
    fun `NonNullSerializer - Garbage (Relaxed) - returns 0L default`() {
        val json = createTestJson(strictMode = false)
        val result = json.decodeFromString(SafeLongSerializer(fakeLogger, false), "\"not-a-number\"")
        assertEquals(0L, result)
    }

    @Test
    fun `NonNullSerializer - Garbage (Strict) - throws exception`() {
        val json = createTestJson(strictMode = true)
        val serializer = SafeLongSerializer(fakeLogger, strictMode = true)
        assertThrows(SerializationException::class.java) {
            json.decodeFromString(serializer, "\"not-a-number\"")
        }
    }

    @Test
    fun `NonNullSerializer - Explicit Null (Relaxed) - returns 0L default`() {
        val json = createTestJson(strictMode = false)
        val result = json.decodeFromString(SafeLongSerializer(fakeLogger, false), "null")
        assertEquals(0L, result)
    }

    @Test
    fun `NonNullSerializer - Explicit Null (Strict) - throws exception`() {
        val json = createTestJson(strictMode = true)
        val serializer = SafeLongSerializer(fakeLogger, strictMode = true)
        assertThrows(SerializationException::class.java) {
            json.decodeFromString(serializer, "null")
        }
    }

    // ============================================================================================
    // Helpers
    // ============================================================================================

    private fun createTestJson(strictMode: Boolean): Json {
        return Json {
            ignoreUnknownKeys = true
            isLenient = true
            serializersModule = SerializersModule {
                contextual(Long::class, SafeLongSerializer(fakeLogger, strictMode))
            }
        }
    }
}
