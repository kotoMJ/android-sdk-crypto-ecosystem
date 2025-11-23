package cz.kotox.crypto.sdk.internal.network

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class SafeSerializersTest {

    @Before
    fun setup() {
        // Reset config before each test
        SerializationConfig.isStrictMode = false
    }

    @After
    fun tearDown() {
        // Clean up static state
        SerializationConfig.isStrictMode = BuildConfig.DEBUG
    }

// ============================================================================================
    // 1. SafeNullableLongSerializer Tests (Field is Long?)
    // ============================================================================================

    @Test
    fun `NullableSerializer - Happy Path - parses valid long (Relaxed)`() {
        configureStrictMode(isStrict = false)
        val json = createTestJson()
        val result = json.decodeFromString(SafeNullableLongSerializer, "123456")
        assertEquals(123456L, result)
    }

    @Test
    fun `NullableSerializer - Happy Path - parses valid long (Strict)`() {
        // Strict mode should NOT throw for valid data
        configureStrictMode(isStrict = true)
        val json = createTestJson()
        val result = json.decodeFromString(SafeNullableLongSerializer, "123456")
        assertEquals(123456L, result)
    }

    @Test
    fun `NullableSerializer - parses explicit null (Relaxed)`() {
        configureStrictMode(isStrict = false)
        val json = createTestJson()
        val result = json.decodeFromString(SafeNullableLongSerializer, "null")
        assertNull(result)
    }

    @Test
    fun `NullableSerializer - parses explicit null (Strict)`() {
        // Strict mode should NOT throw for explicit null on a nullable field
        configureStrictMode(isStrict = true)
        val json = createTestJson()
        val result = json.decodeFromString(SafeNullableLongSerializer, "null")
        assertNull(result)
    }

    @Test
    fun `NullableSerializer - Truncation (Relaxed) - parses double as long`() {
        configureStrictMode(isStrict = false)
        val json = createTestJson()
        val result = json.decodeFromString(SafeNullableLongSerializer, "31.38")
        assertEquals(31L, result)
    }

    @Test
    fun `NullableSerializer - Truncation (Strict) - throws exception`() {
        configureStrictMode(isStrict = true)
        val json = createTestJson()
        val serializer = SafeNullableLongSerializer
        assertThrows(SerializationException::class.java) {
            json.decodeFromString(serializer, "31.38")
        }
    }

    @Test
    fun `NullableSerializer - Garbage Data (Relaxed) - returns null`() {
        configureStrictMode(isStrict = false)
        val json = createTestJson()
        val result = json.decodeFromString(SafeNullableLongSerializer, "\"invalid-string\"")
        assertNull(result)
    }

    @Test
    fun `NullableSerializer - Garbage Data (Strict) - throws exception`() {
        configureStrictMode(isStrict = true)
        val json = createTestJson()
        val serializer = SafeNullableLongSerializer
        assertThrows(SerializationException::class.java) {
            json.decodeFromString(serializer, "\"invalid-string\"")
        }
    }

    // ============================================================================================
    // 2. SafeLongSerializer Tests (Field is Long - Non Null)
    // ============================================================================================

    @Test
    fun `NonNullSerializer - Happy Path - parses valid long (Relaxed)`() {
        configureStrictMode(isStrict = false)
        val json = createTestJson()
        val result = json.decodeFromString(SafeLongSerializer, "999")
        assertEquals(999L, result)
    }

    @Test
    fun `NonNullSerializer - Happy Path - parses valid long (Strict)`() {
        configureStrictMode(isStrict = true)
        val json = createTestJson()
        val result = json.decodeFromString(SafeLongSerializer, "999")
        assertEquals(999L, result)
    }

    @Test
    fun `NonNullSerializer - Truncation (Relaxed) - parses double as long`() {
        configureStrictMode(isStrict = false)
        val json = createTestJson()
        val result = json.decodeFromString(SafeLongSerializer, "99.99")
        assertEquals(99L, result)
    }

    @Test
    fun `NonNullSerializer - Truncation (Strict) - throws exception`() {
        configureStrictMode(isStrict = true)
        val json = createTestJson()
        val serializer = SafeLongSerializer
        assertThrows(SerializationException::class.java) {
            json.decodeFromString(serializer, "99.99")
        }
    }

    @Test
    fun `NonNullSerializer - Garbage (Relaxed) - returns 0L default`() {
        configureStrictMode(isStrict = false)
        val json = createTestJson()
        val result = json.decodeFromString(SafeLongSerializer, "\"not-a-number\"")
        assertEquals(0L, result)
    }

    @Test
    fun `NonNullSerializer - Garbage (Strict) - throws exception`() {
        configureStrictMode(isStrict = true)
        val json = createTestJson()
        val serializer = SafeLongSerializer
        assertThrows(SerializationException::class.java) {
            json.decodeFromString(serializer, "\"not-a-number\"")
        }
    }

    @Test
    fun `NonNullSerializer - Explicit Null (Relaxed) - returns 0L default`() {
        configureStrictMode(isStrict = false)
        val json = createTestJson()
        val result = json.decodeFromString(SafeLongSerializer, "null")
        assertEquals(0L, result)
    }

    @Test
    fun `NonNullSerializer - Explicit Null (Strict) - throws exception`() {
        configureStrictMode(isStrict = true)
        val json = createTestJson()
        val serializer = SafeLongSerializer
        assertThrows(SerializationException::class.java) {
            json.decodeFromString(serializer, "null")
        }
    }

    // ============================================================================================
    // Helpers
    // ============================================================================================

    private fun configureStrictMode(isStrict: Boolean) {
        SerializationConfig.isStrictMode = isStrict
    }

    private fun createTestJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
}
