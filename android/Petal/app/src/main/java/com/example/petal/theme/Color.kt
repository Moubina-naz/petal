package com.example.petal.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

//Light theme

// Backgrounds
val Background      = Color(0xFFFf8f7f2)   // main screen bg
val Surface         = Color(0xFFFFFFFF)   // cards, dialogs, dropdowns
val SurfaceSoft     = Color(0xFFF8F5F1)   // subtle sections, image placeholders

// Primary brand (terracotta)
val Primary             = Color(0xFFd36b54)
val PrimaryContainer    = Color(0xFFE3D1C7)
val OnPrimary           = Color(0xFFFFFFFF)

// Secondary brand (deep green)
val Secondary           = Color(0xFF1E3A2F)
val SecondaryContainer  = Color(0xFF436F60)
val OnSecondary         = Color(0xFFFFFFFF)

// Text
val OnBackground    = Color(0xFF1A1716)   // primary text — warm near-black
val OnSurface       = Color(0xFF3A342F)   // body text on cards ,

val TextSecondary = Color(0xFFA8A29E )

// Borders & outlines
val Outline         = Color(0xFFA09D9B)   // default borders
val OutlineStrong   = Color(0xFF312F2F)   // focused borders, strong borders

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

//bg
val DarkBackground      = Color(0xFF121212 )
val DarkSurface         = Color(0xFF1c1c1c)
val DarkSurfaceSoft     = Color(0xFF332F2B)


//text
val DarkOnBackground    = Color(0xFFf9f7f2)
val DarkOnSurface       = Color(0xFFA8A29E)
val DarkTextSecondary = Color(0xFFA8A29E)

//borders
val DarkOutline         = Color(0xFD2b2b2b)
val DarkOutlineStrong   = Color(0xFF524F4E)

// Primary stays the same terracotta in dark; container gets darker
val DarkPrimaryContainer    = Color(0xFF38231F)
val DarkSecondaryContainer  = Color(0xFF5E8B7E)

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
    val textSecondary: Color,
    val textWhite : Color,
    val surfaceSoft: Color,
    val moodHappy: Color,
    val moodCalm: Color,
    val moodSad: Color,
    val accentLine: Color,
    val navSelected: Color,
    val navUnselected: Color,
    val moodIconColors: Map<String, Color>,
    val orangeBorder: Color,
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
    textSecondary = DarkTextSecondary,
    textWhite = DarkOnBackground,
surfaceSoft    = DarkSurfaceSoft,
    moodHappy      = MoodHappy,
    moodCalm       = MoodCalm,
    moodSad        = MoodSad,
    accentLine     = AccentLine,
    navSelected    = Primary,
    navUnselected  = Color(0xFF999490),
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
    dropdownText = DarkOnSurface,
    orangeBorder = Color(0xFF4f2d27)
)

val LightExtendedColors = ExtendedColors(
    surfaceSoft    = SurfaceSoft,
    textSecondary = TextSecondary ,
    textWhite = DarkOnBackground,
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
    dropdownText = Color(0xFF3A3330),
    orangeBorder = Color(0xFFE2C7B8)


)