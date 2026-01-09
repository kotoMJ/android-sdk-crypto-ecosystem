package cz.kotox.sdk.crypto.app.ui.theme.color

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// --- Base Palette ---
private val CryptoGold = Color(0xFFFDD835) // Main Brand Color
private val CryptoGoldDark = Color(0xFFFBC02D) // For dark mode containers/contrast

// Accessible Gold for Text on Light Backgrounds
private val CryptoGoldAccessible = Color(0xFF917208)

// Dark Palette
private val DarkBaseBackground = Color(0xFF121212)
private val DarkCardSurface = Color(0xFF1E1E1E)
private val DarkTextPrimary = Color(0xFFFFFFFF)
private val DarkTextSecondary = Color(0xFF888888)
private val DarkDivider = Color(0xFF2C2C2C)

// Light Palette (Refined for UX)
private val LightBaseBackground = Color(0xFFF4F6F8) // Soft Cool Grey (Not White!)
private val LightCardSurface = Color(0xFFFFFFFF) // Pure White Cards (Pops against grey bg)
private val LightTextPrimary = Color(0xFF1C1C1E) // Soft Black
private val LightTextSecondary = Color(0xFF6E6E73) // Accessible Grey
private val LightDivider = Color(0xFFE5E5EA)

// Semantics
val PositiveGreen = Color(0xFF34C759)
val NegativeRed = Color(0xFFFF3B30)

// --- Full Light Theme Definition ---
internal val LightColors = lightColorScheme(
    // Primary: The Gold actions
    primary = CryptoGold,
    onPrimary = Color.Black, // Text on Gold is Black
    primaryContainer = CryptoGold,
    onPrimaryContainer = Color.Black,

    // Secondary: Used for less prominent icons/text
    secondary = CryptoGoldAccessible,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE1E4E8),
    onSecondaryContainer = LightTextPrimary,

    // Tertiary: Accents (like charts or stars)
    tertiary = LightTextPrimary,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFEEEEEE),
    onTertiaryContainer = LightTextPrimary,

    // Backgrounds
    background = LightBaseBackground, // <--- KEY CHANGE: Grey Background
    onBackground = LightTextPrimary,

    surface = LightBaseBackground, // Screen surface matches background
    onSurface = LightTextPrimary,

    // Cards (Mapped to surfaceVariant in your code)
    surfaceVariant = LightCardSurface, // <--- KEY CHANGE: White Cards
    onSurfaceVariant = LightTextSecondary,

    // Errors
    error = NegativeRed,
    errorContainer = Color(0xFFFFEBEE),
    onError = Color.White,
    onErrorContainer = Color(0xFFC62828),

    // Outlines (Dividers/Borders)
    outline = LightDivider,
    outlineVariant = Color(0xFFC6C6C8),

    // Extras
    scrim = Color.Black,
    inverseSurface = DarkCardSurface,
    inverseOnSurface = DarkTextPrimary,
    inversePrimary = CryptoGold,
)

// --- Full Dark Theme Definition ---
internal val DarkColors = darkColorScheme(
    // Primary
    primary = CryptoGold,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF3E3000), // Deep muted gold
    onPrimaryContainer = CryptoGold,

    // Secondary
    secondary = CryptoGold,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF333333),
    onSecondaryContainer = Color.White,

    // Tertiary
    tertiary = Color(0xFFB1C5FF),
    onTertiary = Color(0xFF002C71),
    tertiaryContainer = Color(0xFF00419E),
    onTertiaryContainer = Color(0xFFDAE2FF),

    // Backgrounds
    background = DarkBaseBackground,
    onBackground = DarkTextPrimary,

    surface = DarkBaseBackground,
    onSurface = DarkTextPrimary,

    // Cards
    surfaceVariant = DarkCardSurface,
    onSurfaceVariant = DarkTextSecondary,

    // Errors
    error = NegativeRed,
    errorContainer = Color(0xFF93000A),
    onError = Color.Black,
    onErrorContainer = Color(0xFFFFDAD6),

    // Outlines
    outline = DarkDivider,
    outlineVariant = Color(0xFF444444),

    // Extras
    scrim = Color.Black,
    inverseSurface = LightBaseBackground,
    inverseOnSurface = LightTextPrimary,
    inversePrimary = CryptoGoldDark,
)
