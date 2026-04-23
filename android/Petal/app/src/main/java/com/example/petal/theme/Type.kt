package com.example.petal.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.petal.R

// Set of Material typography styles to start with
private val Playfair = FontFamily(
    Font(R.font.playfairdisplay_regular, FontWeight.Normal),
    Font(R.font.playfairdisplay_medium, FontWeight.Medium)
)
private val Inter = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium)
)
val Typography = Typography(
    
    // App titles (Petal, Calendar)
    headlineLarge = TextStyle(
        fontFamily = Playfair,
        fontWeight = FontWeight.Medium,
        fontSize = 28.sp   // ⬅ reduced from 32 (less dramatic jump)
    ),

    // Screen titles (Edit Profile, Change Password)
    titleLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),

    // Section headers (TODAY, EARLIER THIS WEEK)
    titleMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,     // ⬅ slightly smaller
        letterSpacing = 0.5.sp // ⬅ reduced (was too wide)
    ),

    // Card titles (memory titles)
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp   // ⬅ reduced from 16 (better balance)
    ),

    // Secondary text (notes)
    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp   // ⬅ reduced from 14
    ),

    // Metadata (time, tags, small labels)
    bodySmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp
    ),

    // UI labels (chips, inputs)
    labelMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    )
)