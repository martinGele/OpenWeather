package com.sporty.openweather.core.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Atmospheric-sky palette. The backdrop is a live gradient ([Sky]); content
 * floats on it as frosted glass with white type. Rausch (#ff385c) is the one
 * accent that reads on every sky. See airbnb/DESIGN.md.
 */

// Brand voltage — Rausch (survives on any sky)
val Rausch = Color(0xFFFF385C)
val RauschActive = Color(0xFFE00B41)
val RauschDisabled = Color(0xFFFFD1DA)
val OnPrimary = Color(0xFFFFFFFF)

// On-sky text (white family)
val OnSky = Color(0xFFFFFFFF)
val OnSkyMuted = Color(0xFFFFFFFF).copy(alpha = 0.78f)
val OnSkyFaint = Color(0xFFFFFFFF).copy(alpha = 0.60f)

// Frosted glass surfaces
val GlassFill = Color(0xFFFFFFFF).copy(alpha = 0.16f)
val GlassFillStrong = Color(0xFFFFFFFF).copy(alpha = 0.22f)
val GlassBorder = Color(0xFFFFFFFF).copy(alpha = 0.30f)

// Pale warm error tint — stays readable on a dark storm sky.
val ErrorOnSky = Color(0xFFFFD7CF)

// Neutral ink palette — kept for the (unused-by-default) Material scheme fallbacks.
val Ink = Color(0xFF222222)
val Muted = Color(0xFF6A6A6A)
val Canvas = Color(0xFFFFFFFF)
