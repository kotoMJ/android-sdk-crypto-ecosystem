@file:Suppress("MatchingDeclarationName")

package cz.kotox.sdk.crypto.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import cz.kotox.sdk.crypto.app.R

val SatoshiFontFamily = FontFamily(
    // --- Upright (Normal) ---
    Font(R.font.satoshi_light, FontWeight.Light),
    Font(R.font.satoshi_regular, FontWeight.Normal),
    Font(R.font.satoshi_medium, FontWeight.Medium),
    Font(R.font.satoshi_bold, FontWeight.Bold),
    Font(R.font.satoshi_black, FontWeight.Black),
    // --- Italics (Map these to Style.Italic) ---
    Font(R.font.satoshi_light_italic, FontWeight.Light, style = FontStyle.Italic),
    Font(R.font.satoshi_italic, FontWeight.Normal, style = FontStyle.Italic), // corresponds to Regular Italic
    Font(R.font.satoshi_medium_italic, FontWeight.Medium, style = FontStyle.Italic),
    Font(R.font.satoshi_bold_italic, FontWeight.Bold, style = FontStyle.Italic),
    Font(R.font.satoshi_black_italic, FontWeight.Black, style = FontStyle.Italic),
)

private val defaultTextStyle = TextStyle(
    fontFamily = SatoshiFontFamily,
    fontWeight = FontWeight.Normal,
    letterSpacing = 0.sp,
)

val AppTypography = Typography(
    // DISPLAY: Usually regular or light for very large text
    displayLarge = defaultTextStyle.copy(
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
        fontWeight = FontWeight.Normal, // Uses satoshi_regular
    ),
    displayMedium = defaultTextStyle.copy(
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Normal,
    ),
    displaySmall = defaultTextStyle.copy(
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Normal,
    ),

    // HEADLINE: Often regular or bold depending on brand. Let's use Bold for impact.
    headlineLarge = defaultTextStyle.copy(
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Bold, // <--- Uses satoshi_bold
    ),
    headlineMedium = defaultTextStyle.copy(
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Bold, // <--- Uses satoshi_bold
    ),
    headlineSmall = defaultTextStyle.copy(
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Bold, // <--- Uses satoshi_bold
    ),

    // TITLE: Medium emphasis
    titleLarge = defaultTextStyle.copy(
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Medium, // <--- Uses satoshi_medium
    ),
    titleMedium = defaultTextStyle.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
        fontWeight = FontWeight.Medium, // <--- Uses satoshi_medium
    ),
    titleSmall = defaultTextStyle.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
        fontWeight = FontWeight.Medium,
    ),

    // BODY: Reading text is usually Normal or Light
    bodyLarge = defaultTextStyle.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        fontWeight = FontWeight.Normal, // Uses satoshi_regular
    ),
    bodyMedium = defaultTextStyle.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
        fontWeight = FontWeight.Normal,
    ),
    bodySmall = defaultTextStyle.copy(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
        fontWeight = FontWeight.Normal,
    ),

    // LABEL: Used for buttons and tags. Usually Medium or Bold.
    labelLarge = defaultTextStyle.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
        fontWeight = FontWeight.Bold, // <--- Uses satoshi_bold (Great for Buttons!)
    ),
    labelMedium = defaultTextStyle.copy(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        fontWeight = FontWeight.Medium,
    ),
    labelSmall = defaultTextStyle.copy(
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        fontWeight = FontWeight.Medium,
    ),
)
