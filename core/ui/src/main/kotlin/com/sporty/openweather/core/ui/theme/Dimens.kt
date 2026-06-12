package com.sporty.openweather.core.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Spacing — 4px base with a 2px micro-step. Editorial bands breathe at [section]
 * (64dp) while card grids stay dense. See airbnb/DESIGN.md > Layout.
 */
object Spacing {
    val xxs = 2.dp
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val base = 16.dp
    val lg = 24.dp
    val xl = 32.dp
    val xxl = 48.dp
    val section = 64.dp
}

/**
 * Soft shape language — buttons at [sm] (8dp), cards at [md] (14dp), and pills /
 * orbs / circular controls at [full]. Essentially no hard corner anywhere.
 */
object Radius {
    val none = 0.dp
    val xs = 4.dp
    val sm = 8.dp
    val md = 14.dp
    val lg = 20.dp
    val xl = 32.dp
    val full = 9999.dp
}
