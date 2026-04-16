package com.example.petal.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    background      = Background,
    surface         = Surface,
    surfaceVariant  = SurfaceSoft,

    primary             = Primary,
    primaryContainer    = PrimaryContainer,
    onPrimary           = OnPrimary,

    secondary           = Secondary,
    secondaryContainer  = SecondaryContainer,
    onSecondary         = OnSecondary,

    onBackground        = OnBackground,
    onSurface           = OnSurface,

    outline             = Outline,
    outlineVariant      = OutlineStrong,

    error               = Error,
    errorContainer      = ErrorContainer,

)

private val DarkColorScheme = darkColorScheme(
    background      = DarkBackground,
    surface         = DarkSurface,
    surfaceVariant  = DarkSurfaceSoft,

    primary             = Primary,           // terracotta holds in dark
    primaryContainer    = DarkPrimaryContainer,
    onPrimary           = Color.White,

    secondary           = Secondary,
    secondaryContainer  = DarkSecondaryContainer,
    onSecondary         = Color.White,

    onBackground        = DarkOnBackground,
    onSurface           = DarkOnSurface,

    outline             = DarkOutline,
    outlineVariant      = DarkOutlineStrong,

    error               = DarkError,
    errorContainer      = DarkErrorContainer,
)

@Composable
fun PetalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else      -> LightColorScheme
    }

    // Provide extended colors alongside the M3 scheme
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography  = Typography,
            content     = content
        )
    }
}
val MaterialTheme.extended: ExtendedColors
    @Composable get() = LocalExtendedColors.current
