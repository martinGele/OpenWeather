package com.sporty.openweather.core.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * The data-driven backdrop. A weather condition crossed with day/night selects a
 * 3-stop vertical gradient — the screen *becomes* the weather. Every gradient is
 * kept deep enough that white type holds contrast across its full height.
 * See airbnb/DESIGN.md > Sky Backdrop.
 */
object Sky {

    /** Default backdrop when there is no observation yet (loading, search screen). */
    val default: List<Color> = clearDay

    /** Coarse weather family — shared by [gradientFor] and the animated sky layer. */
    fun kindOf(condition: String): SkyKind {
        val c = condition.lowercase()
        return when {
            "thunder" in c -> SkyKind.STORM
            "snow" in c -> SkyKind.SNOW
            "rain" in c || "drizzle" in c -> SkyKind.RAIN
            "cloud" in c -> SkyKind.CLOUDS
            "mist" in c || "fog" in c || "haze" in c || "smoke" in c -> SkyKind.MIST
            else -> SkyKind.CLEAR
        }
    }

    /**
     * @param condition OpenWeather condition group, e.g. "Clear", "Rain", "Snow".
     * @param isDay     whether the observation falls between sunrise and sunset.
     */
    fun gradientFor(condition: String, isDay: Boolean): List<Color> =
        when (kindOf(condition)) {
            SkyKind.STORM -> storm
            SkyKind.SNOW -> if (isDay) snowDay else snowNight
            SkyKind.RAIN -> if (isDay) rainDay else rainNight
            SkyKind.CLOUDS -> if (isDay) cloudsDay else cloudsNight
            SkyKind.MIST -> if (isDay) mistDay else mistNight
            SkyKind.CLEAR -> if (isDay) clearDay else clearNight
        }
}

enum class SkyKind { CLEAR, CLOUDS, RAIN, STORM, SNOW, MIST }

private val clearDay = listOf(Color(0xFF1E78C2), Color(0xFF4FA0DC), Color(0xFF8FC5EF))
private val clearNight = listOf(Color(0xFF0B1026), Color(0xFF1B2450), Color(0xFF34407A))
private val cloudsDay = listOf(Color(0xFF5B6B78), Color(0xFF8595A1), Color(0xFFAAB7C0))
private val cloudsNight = listOf(Color(0xFF1B232C), Color(0xFF313C47), Color(0xFF49555F))
private val rainDay = listOf(Color(0xFF3E5A6E), Color(0xFF5C7C92), Color(0xFF7C9AAD))
private val rainNight = listOf(Color(0xFF141B26), Color(0xFF26323F), Color(0xFF38485A))
private val storm = listOf(Color(0xFF20242B), Color(0xFF3A3F4A), Color(0xFF565D6B))
private val snowDay = listOf(Color(0xFF5E7A92), Color(0xFF88A6BE), Color(0xFFB9CEDD))
private val snowNight = listOf(Color(0xFF273140), Color(0xFF3F4D60), Color(0xFF56697E))
private val mistDay = listOf(Color(0xFF8794A0), Color(0xFFA9B4BD), Color(0xFFC6CDD3))
private val mistNight = listOf(Color(0xFF2A323A), Color(0xFF3A434C), Color(0xFF4A555E))
