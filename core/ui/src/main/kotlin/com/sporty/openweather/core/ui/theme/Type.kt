package com.sporty.openweather.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Type voice (airbnb/DESIGN.md > Typography). Airbnb Cereal VF is licensed and
 * unavailable; the design names Inter as the closest substitute and system sans
 * as the underlying stack, so we fall through to the platform sans.
 *
 * Weights stay modest — display sits at 500–700, body at 400. The one loud moment
 * is the rating display (64sp / 700), which we lend to the hero temperature.
 */
private val Cereal = FontFamily.SansSerif

val Typography = Typography(
    // rating-display — the single loud moment (hero temperature)
    displayLarge = TextStyle(
        fontFamily = Cereal,
        fontWeight = FontWeight.Bold,
        fontSize = 64.sp,
        lineHeight = 70.sp,
        letterSpacing = (-1).sp,
    ),
    // display-xl — page h1
    displayMedium = TextStyle(
        fontFamily = Cereal,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        letterSpacing = 0.sp,
    ),
    // display-lg — detail h1
    displaySmall = TextStyle(
        fontFamily = Cereal,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 26.sp,
        letterSpacing = (-0.4).sp,
    ),
    // display-md — section heads
    headlineMedium = TextStyle(
        fontFamily = Cereal,
        fontWeight = FontWeight.Bold,
        fontSize = 21.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    // display-sm — sub-section titles
    titleLarge = TextStyle(
        fontFamily = Cereal,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.18).sp,
    ),
    // title-md — block titles
    titleMedium = TextStyle(
        fontFamily = Cereal,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),
    // title-sm — column heads
    titleSmall = TextStyle(
        fontFamily = Cereal,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),
    // body-md — running copy
    bodyLarge = TextStyle(
        fontFamily = Cereal,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),
    // body-sm — card meta, dates, prices
    bodyMedium = TextStyle(
        fontFamily = Cereal,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),
    // caption-sm — legal line
    bodySmall = TextStyle(
        fontFamily = Cereal,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
    ),
    // button-md — CTA labels
    labelLarge = TextStyle(
        fontFamily = Cereal,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),
    // caption — segment labels
    labelMedium = TextStyle(
        fontFamily = Cereal,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp,
    ),
    // badge — floating badge text
    labelSmall = TextStyle(
        fontFamily = Cereal,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 13.sp,
        letterSpacing = 0.sp,
    ),
)
