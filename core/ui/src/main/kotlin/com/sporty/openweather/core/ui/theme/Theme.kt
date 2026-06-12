package com.sporty.openweather.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/**
 * The atmospheric system paints its own full-bleed gradient ([Sky]) and renders
 * content in white-on-glass, so the Material color scheme mostly supplies sane
 * defaults. Rausch is the one accent; no dark mode, no dynamic color.
 */
private val OpenWeatherColorScheme = lightColorScheme(
    primary = Rausch,
    onPrimary = OnPrimary,
    primaryContainer = RauschDisabled,
    background = Canvas,
    onBackground = Ink,
    surface = Canvas,
    onSurface = Ink,
    onSurfaceVariant = Muted,
    outline = GlassBorder,
    error = ErrorOnSky,
    onError = Ink,
)

/** Shared app theme. Lives in the core:ui module so every feature reuses it. */
@Composable
fun OpenWeatherTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = OpenWeatherColorScheme,
        typography = Typography,
        content = content,
    )
}
