package com.example.petal.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

//Light theme

// Backgrounds
val Background      = Color(0xFFF4EFEA)   // main screen bg
val Surface         = Color(0xFFFFFFFF)   // cards, dialogs, dropdowns
val SurfaceSoft     = Color(0xFFF8F5F1)   // subtle sections, image placeholders

// Primary brand (terracotta)
val Primary             = Color(0xFFC65D5D)
val PrimaryContainer    = Color(0xFFE8B5A5)
val OnPrimary           = Color(0xFFFFFFFF)
val black = Color(0xFF2d2d2d)

// Secondary brand (deep green)
val Secondary           = Color(0xFF1E3A2F)
val SecondaryContainer  = Color(0xFFa0bbb3)
val OnSecondary         = Color(0xFFFFFFFF)

// Text
val OnBackground    = Color(0xFF2B2623)   // primary text — warm near-black
val OnSurface       = Color(0xFF3A342F)   // body text on cards
val TextMuted       = Color(0xFF888888)   // placeholder, captions, labels

// Borders & outlines
val Outline         = Color(0xFFD8D2CC)   // default borders
val OutlineStrong   = Color(0xFF2d2d2d)   // focused borders, strong borders

// Destructive / error
val Error           = Color(0xFFB25B5B)   // delete, errors, logout accent
val ErrorContainer  = Color(0xFFFFEDED)   // error icon bg

// Mood status colors
val MoodHappy   = Color(0xFFE8AFAF)
val MoodCalm    = Color(0xFFE6D8A8)
val MoodSad     = Color(0xFFA8C4BE)

// Accent line (used in memory detail date strip)
val AccentLine  = Color(0xFFCB6040)
//Mood icon colors
val MoodCalmIcon      = Color(0xFFD6A54A)
val MoodHappyIcon     = Color(0xFFD86C5B)
val MoodSadIcon       = Color(0xFF5C8D82)
val MoodAnxiousIcon   = Color(0xFFC97A4C)
val MoodExcitedIcon   = Color(0xFFC75B39)
val MoodReflectiveIcon= Color(0xFF6F8F7A)
val MoodGratefulIcon  = Color(0xFFC9A44F)
val MoodAngryIcon     = Color(0xFFA64B3C)
val MoodLonelyIcon    = Color(0xFF7C8A96)
val MoodContentIcon   = Color(0xFF8A9A5B)

//Dark theme
val DarkBackground      = Color(0xFF1E1B18)
val DarkSurface         = Color(0xFF2A2623)
val DarkSurfaceSoft     = Color(0xFF332F2B)

val DarkOnBackground    = Color(0xFFEDE7E3)
val DarkOnSurface       = Color(0xFFD6D0CB)
val DarkTextMuted       = Color(0xFF9A938D)

val DarkOutline         = Color(0xFF4A443F)
val DarkOutlineStrong   = Color(0xFFD6D0CB)

// Primary stays the same terracotta in dark; container gets darker
val DarkPrimaryContainer    = Color(0xFF5C3A3A)
val DarkSecondaryContainer  = Color(0xFF2F4A42)

val DarkError           = Color(0xFFCF7070)
val DarkErrorContainer  = Color(0xFF3D2020)

//Dark mood icon colors
val DarkMoodCalmIcon      = Color(0xFFE8BA62)
val DarkMoodHappyIcon     = Color(0xFFE8826E)
val DarkMoodSadIcon       = Color(0xFF72A89C)
val DarkMoodAnxiousIcon   = Color(0xFFDD9060)
val DarkMoodExcitedIcon   = Color(0xFFD97255)
val DarkMoodReflectiveIcon= Color(0xFF88AA94)
val DarkMoodGratefulIcon  = Color(0xFFDDBB62)
val DarkMoodAngryIcon     = Color(0xFFBF6055)
val DarkMoodLonelyIcon    = Color(0xFF96A4AE)
val DarkMoodContentIcon   = Color(0xFFA0B46E)

@Immutable
data class ExtendedColors(
    val textMuted: Color,
    val surfaceSoft: Color,
    val moodHappy: Color,
    val moodCalm: Color,
    val moodSad: Color,
    val accentLine: Color,
    val navSelected: Color,
    val navUnselected: Color,

    val moodIconColors: Map<String, Color>,

    val dropdownSelectedBg: Color,
    val dropdownIconBg: Color,
    val dropdownIconTint: Color,
    val dropdownText: Color
)


val LocalExtendedColors = staticCompositionLocalOf {
    LightExtendedColors
}

// Dark variants
val DarkExtendedColors = ExtendedColors(
    textMuted      = DarkTextMuted,
    surfaceSoft    = DarkSurfaceSoft,
    moodHappy      = MoodHappy,
    moodCalm       = MoodCalm,
    moodSad        = MoodSad,
    accentLine     = AccentLine,
    navSelected    = Primary,
    navUnselected  = DarkOutline,
    moodIconColors = mapOf(
        "CALM"       to DarkMoodCalmIcon,
        "HAPPY"      to DarkMoodHappyIcon,
        "SAD"        to DarkMoodSadIcon,
        "ANXIOUS"    to DarkMoodAnxiousIcon,
        "EXCITED"    to DarkMoodExcitedIcon,
        "REFLECTIVE" to DarkMoodReflectiveIcon,
        "GRATEFUL"   to DarkMoodGratefulIcon,
        "ANGRY"      to DarkMoodAngryIcon,
        "LONELY"     to DarkMoodLonelyIcon,
        "CONTENT"    to DarkMoodContentIcon,
    ),
    dropdownSelectedBg = Color(0xFF4A3F1F),
    dropdownIconBg = DarkSurface,
    dropdownIconTint = DarkOnSurface,
    dropdownText = DarkOnSurface
)

val LightExtendedColors = ExtendedColors(
    textMuted      = TextMuted,
    surfaceSoft    = SurfaceSoft,
    moodHappy      = MoodHappy,
    moodCalm       = MoodCalm,
    moodSad        = MoodSad,
    accentLine     = AccentLine,
    navSelected    = Color(0xFFE86A33),
    navUnselected  = Outline,
    moodIconColors = mapOf(
        "CALM"       to MoodCalmIcon,
        "HAPPY"      to MoodHappyIcon,
        "SAD"        to MoodSadIcon,
        "ANXIOUS"    to MoodAnxiousIcon,
        "EXCITED"    to MoodExcitedIcon,
        "REFLECTIVE" to MoodReflectiveIcon,
        "GRATEFUL"   to MoodGratefulIcon,
        "ANGRY"      to MoodAngryIcon,
        "LONELY"     to MoodLonelyIcon,
        "CONTENT"    to MoodContentIcon,
    ) ,
    dropdownSelectedBg = Color(0xFFF6E7A8),
    dropdownIconBg = Color.White,
    dropdownIconTint = Color(0xFF615A57),
    dropdownText = Color(0xFF3A3330)
)