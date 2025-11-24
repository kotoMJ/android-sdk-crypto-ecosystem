package cz.kotox.crypto.sdk.internal.network

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class SafeSerializersTest {

    @Before
    fun setup() {
        //  before each test
    }

    @After
    fun tearDown() {
        // Clean up static state
    }

// ============================================================================================
    // 1. SafeNullableLongSerializer Tests (Field is Long?)
    // ============================================================================================

    @Test
    fun `NullableSerializer - Happy Path - parses valid long (Relaxed)`() {
        val json = createTestJson(isStrict = false)
        val result = json.decodeFromString(SafeNullableLongSerializerDefault, "123456")
        assertEquals(123456L, result)
    }

    @Test
    fun `NullableSerializer - Happy Path - parses valid long (Strict)`() {
        // Strict mode should NOT throw for valid data
        val json = createTestJson(isStrict = true)
        val result = json.decodeFromString(SafeNullableLongSerializerDefault, "123456")
        assertEquals(123456L, result)
    }

    @Test
    fun `NullableSerializer - parses explicit null (Relaxed)`() {
        val json = createTestJson(isStrict = false)
        val result = json.decodeFromString(SafeNullableLongSerializerDefault, "null")
        assertNull(result)
    }

    @Test
    fun `NullableSerializer - parses explicit null (Strict)`() {
        // Strict mode should NOT throw for explicit null on a nullable field
        val json = createTestJson(isStrict = true)
        val result = json.decodeFromString(SafeNullableLongSerializerDefault, "null")
        assertNull(result)
    }

    @Test
    fun `NullableSerializer - Truncation (Relaxed) - parses double as long`() {
        val json = createTestJson(isStrict = false)
        val result = json.decodeFromString(SafeNullableLongSerializerDefault, "31.38")
        assertEquals(31L, result)
    }

    @Test
    fun `NullableSerializer - Truncation (Strict) - throws exception`() {
        val json = createTestJson(isStrict = true)
        val serializer = SafeNullableLongSerializerDefault
        assertThrows(SerializationException::class.java) {
            json.decodeFromString(serializer, "31.38")
        }
    }

    @Test
    fun `NullableSerializer - Garbage Data (Relaxed) - returns null`() {
        val json = createTestJson(isStrict = false)
        val result = json.decodeFromString(SafeNullableLongSerializerDefault, "\"invalid-string\"")
        assertNull(result)
    }

    @Test
    fun `NullableSerializer - Garbage Data (Strict) - throws exception`() {
        val json = createTestJson(isStrict = true)
        val serializer = SafeNullableLongSerializerDefault
        assertThrows(SerializationException::class.java) {
            json.decodeFromString(serializer, "\"invalid-string\"")
        }
    }

    // ============================================================================================
    // 2. SafeLongSerializer Tests (Field is Long - Non Null)
    // ============================================================================================

    @Test
    fun `NonNullSerializer - Happy Path - parses valid long (Relaxed)`() {
        val json = createTestJson(isStrict = false)
        val result = json.decodeFromString(SafeLongSerializerDefault, "999")
        assertEquals(999L, result)
    }

    @Test
    fun `NonNullSerializer - Happy Path - parses valid long (Strict)`() {
        val json = createTestJson(isStrict = true)
        val result = json.decodeFromString(SafeLongSerializerDefault, "999")
        assertEquals(999L, result)
    }

    @Test
    fun `NonNullSerializer - Truncation (Relaxed) - parses double as long`() {
        val json = createTestJson(isStrict = false)
        val result = json.decodeFromString(SafeLongSerializerDefault, "99.99")
        assertEquals(99L, result)
    }

    @Test
    fun `NonNullSerializer - Truncation (Strict) - throws exception`() {
        val json = createTestJson(isStrict = true)
        val serializer = SafeLongSerializerDefault
        assertThrows(SerializationException::class.java) {
            json.decodeFromString(serializer, "99.99")
        }
    }

    @Test
    fun `NonNullSerializer - Garbage (Relaxed) - returns 0L default`() {
        val json = createTestJson(isStrict = false)
        val result = json.decodeFromString(SafeLongSerializerDefault, "\"not-a-number\"")
        assertEquals(0L, result)
    }

    @Test
    fun `NonNullSerializer - Garbage (Strict) - throws exception`() {
        val json = createTestJson(isStrict = true)
        val serializer = SafeLongSerializerDefault
        assertThrows(SerializationException::class.java) {
            json.decodeFromString(serializer, "\"not-a-number\"")
        }
    }

    @Test
    fun `NonNullSerializer - Explicit Null (Relaxed) - returns 0L default`() {
        val json = createTestJson(isStrict = false)
        val result = json.decodeFromString(SafeLongSerializerDefault, "null")
        assertEquals(0L, result)
    }

    @Test
    fun `NonNullSerializer - Explicit Null (Strict) - throws exception`() {
        val json = createTestJson(isStrict = true)
        val serializer = SafeLongSerializerDefault
        assertThrows(SerializationException::class.java) {
            json.decodeFromString(serializer, "null")
        }
    }

    // ============================================================================================
    // Helpers
    // ============================================================================================

    /**
     * Creates a Json instance that mimics the production behavior.
     * If [isStrict] is true, we register the StrictModeMarker in the SerializersModule.
     */
    private fun createTestJson(isStrict: Boolean): Json {
        return Json {
            ignoreUnknownKeys = true
            isLenient = true

            // This logic mirrors KtorfitFactory
            serializersModule = SerializersModule {
                if (isStrict) {
                    contextual(StrictModeMarker::class, StrictModeMarker.serializer())
                }
            }
        }
    }
}
