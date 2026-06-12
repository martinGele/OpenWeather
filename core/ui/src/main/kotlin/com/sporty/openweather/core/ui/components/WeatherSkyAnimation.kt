package com.sporty.openweather.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import com.sporty.openweather.core.ui.theme.Sky
import com.sporty.openweather.core.ui.theme.SkyKind
import kotlin.math.floor
import kotlin.math.sin
import kotlin.random.Random

/**
 * A subtle animated layer that sits over the [Sky] gradient and *acts out* the
 * weather: clouds drift, rain and snow fall, the sun's rays turn, stars twinkle,
 * mist rolls, and storms flash. All shapes are translucent white so they read on
 * any gradient without competing with the content. See airbnb/DESIGN.md.
 *
 * Driven by one per-frame clock ([rememberFrameSeconds]); every motion is derived
 * analytically from that time so particles wrap seamlessly off-screen.
 */
@Composable
fun WeatherSkyAnimation(
    condition: String,
    isDay: Boolean,
    modifier: Modifier = Modifier,
    sunHeightFraction: Float = 0.22f,
) {
    val time by rememberFrameSeconds()

    Box(modifier = modifier.fillMaxSize()) {
        when (Sky.kindOf(condition)) {
            SkyKind.CLEAR -> if (isDay) SunLayer(time, sunHeightFraction) else StarLayer(time)
            SkyKind.CLOUDS -> CloudLayer(time, count = 5)
            SkyKind.RAIN -> {
                CloudLayer(time, count = 4)
                PrecipitationLayer(time, snow = false)
            }
            SkyKind.STORM -> {
                CloudLayer(time, count = 5)
                PrecipitationLayer(time, snow = false)
                LightningLayer(time)
            }
            SkyKind.SNOW -> {
                CloudLayer(time, count = 3)
                PrecipitationLayer(time, snow = true)
            }
            SkyKind.MIST -> MistLayer(time)
        }
    }
}

/** Seconds elapsed since first composition, updated every frame. */
@Composable
private fun rememberFrameSeconds(): State<Float> {
    val seconds = remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        val startNanos = withFrameNanos { it }
        while (true) {
            withFrameNanos { now -> seconds.floatValue = (now - startNanos) / 1_000_000_000f }
        }
    }
    return seconds
}

private fun frac(v: Float): Float = v - floor(v)

// ---- Clouds -------------------------------------------------------------------

private class Cloud(val baseY: Float, val scale: Float, val speed: Float, val phase: Float, val alpha: Float)

@Composable
private fun CloudLayer(time: Float, count: Int) {
    val clouds = remember(count) {
        val r = Random(count * 31 + 7)
        List(count) {
            Cloud(
                baseY = 0.05f + r.nextFloat() * 0.40f,
                scale = 0.65f + r.nextFloat() * 0.7f,
                speed = 0.012f + r.nextFloat() * 0.02f,
                phase = r.nextFloat(),
                alpha = 0.14f + r.nextFloat() * 0.12f,
            )
        }
    }
    Canvas(Modifier.fillMaxSize()) {
        clouds.forEach { cloud ->
            val x = (-0.4f + frac(time * cloud.speed + cloud.phase) * 1.8f) * size.width
            val y = cloud.baseY * size.height
            drawCloud(Offset(x, y), cloud.scale * size.minDimension * 0.11f, cloud.alpha)
        }
    }
}

// Relative puff layout (dx, dy, radius) in units of the base radius — a lumpy top
// over a flat-ish base, the classic cloud silhouette.
private val cloudPuffs = listOf(
    Triple(0.0f, 0.10f, 1.05f),
    Triple(-1.15f, 0.28f, 0.72f),
    Triple(1.20f, 0.24f, 0.80f),
    Triple(1.85f, 0.48f, 0.52f),
    Triple(-1.75f, 0.46f, 0.50f),
    Triple(-0.45f, -0.45f, 0.66f),
    Triple(0.55f, -0.40f, 0.64f),
)

/**
 * A soft, fluffy cloud. Every puff is a feathered radial gradient, and the whole
 * cloud is drawn into a single alpha layer so overlapping puffs read as one body
 * (no darkened seams) with a denser core and wispy edges.
 */
private fun DrawScope.drawCloud(center: Offset, r: Float, alpha: Float) {
    val canvas = drawContext.canvas
    val bounds = Rect(
        Offset(center.x - r * 3f, center.y - r * 2f),
        Size(r * 6f, r * 4f),
    )
    canvas.saveLayer(bounds, Paint().apply { this.alpha = alpha })
    cloudPuffs.forEach { (dx, dy, pr) ->
        val c = Offset(center.x + dx * r, center.y + dy * r)
        val radius = pr * r * 1.25f
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0f to Color.White,
                    0.6f to Color.White,
                    1f to Color.Transparent,
                ),
                center = c,
                radius = radius,
            ),
            radius = radius,
            center = c,
        )
    }
    canvas.restore()
}

// ---- Rain / Snow --------------------------------------------------------------

private class Particle(val x: Float, val len: Float, val speed: Float, val phase: Float, val sway: Float)

@Composable
private fun PrecipitationLayer(time: Float, snow: Boolean) {
    val particles = remember(snow) {
        val r = Random(if (snow) 99 else 42)
        val count = if (snow) 70 else 110
        List(count) {
            Particle(
                x = r.nextFloat(),
                len = if (snow) 0f else 0.02f + r.nextFloat() * 0.03f,
                speed = (if (snow) 0.10f else 0.55f) + r.nextFloat() * (if (snow) 0.10f else 0.45f),
                phase = r.nextFloat(),
                sway = r.nextFloat() * 6.28f,
            )
        }
    }
    Canvas(Modifier.fillMaxSize()) {
        particles.forEach { p ->
            val y = frac(time * p.speed + p.phase) * 1.1f - 0.05f
            if (snow) {
                val swayX = sin(time * 0.8f + p.sway) * 0.015f
                val cx = (p.x + swayX) * size.width
                drawCircle(
                    color = Color.White.copy(alpha = 0.85f),
                    radius = size.minDimension * (0.004f + p.phase * 0.004f),
                    center = Offset(cx, y * size.height),
                )
            } else {
                val cx = p.x * size.width
                val top = y * size.height
                drawLine(
                    color = Color.White.copy(alpha = 0.32f),
                    start = Offset(cx, top),
                    end = Offset(cx + size.width * 0.012f, top + p.len * size.height),
                    strokeWidth = size.minDimension * 0.004f,
                    cap = StrokeCap.Round,
                )
            }
        }
    }
}

// ---- Lightning ----------------------------------------------------------------

@Composable
private fun LightningLayer(time: Float) {
    // A quick double-flash roughly every 6 seconds.
    val cycle = frac(time / 6f)
    val flash = when {
        cycle < 0.04f -> 1f - cycle / 0.04f
        cycle in 0.08f..0.12f -> 1f - (cycle - 0.08f) / 0.04f
        else -> 0f
    }
    if (flash > 0f) {
        Canvas(Modifier.fillMaxSize()) {
            drawRect(color = Color.White.copy(alpha = 0.18f * flash))
        }
    }
}

// ---- Sun ----------------------------------------------------------------------

@Composable
private fun SunLayer(time: Float, heightFraction: Float) {
    Canvas(Modifier.fillMaxSize()) {
        // Sits below the top search pill, in the empty space beside the location title.
        val center = Offset(size.width * 0.82f, size.height * heightFraction)
        val sunR = size.minDimension * 0.10f
        // Soft glow.
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White.copy(alpha = 0.40f), Color.Transparent),
                center = center,
                radius = sunR * 2.2f,
            ),
            radius = sunR * 2.2f,
            center = center,
        )
        drawCircle(Color.White.copy(alpha = 0.55f), radius = sunR, center = center)
        // Slowly turning rays.
        rotate(degrees = time * 6f, pivot = center) {
            repeat(12) { i ->
                rotate(degrees = i * 30f, pivot = center) {
                    drawLine(
                        color = Color.White.copy(alpha = 0.22f),
                        start = center + Offset(0f, -sunR * 1.5f),
                        end = center + Offset(0f, -sunR * 2.2f),
                        strokeWidth = size.minDimension * 0.006f,
                        cap = StrokeCap.Round,
                    )
                }
            }
        }
    }
}

// ---- Stars (clear night) ------------------------------------------------------

private class Star(val x: Float, val y: Float, val r: Float, val speed: Float, val phase: Float)

@Composable
private fun StarLayer(time: Float) {
    val stars = remember {
        val rnd = Random(2026)
        List(46) {
            Star(
                x = rnd.nextFloat(),
                y = rnd.nextFloat() * 0.7f,
                r = 0.6f + rnd.nextFloat() * 1.8f,
                speed = 0.8f + rnd.nextFloat() * 1.6f,
                phase = rnd.nextFloat() * 6.28f,
            )
        }
    }
    Canvas(Modifier.fillMaxSize()) {
        // Moon with a soft halo.
        val moon = Offset(size.width * 0.78f, size.height * 0.14f)
        val moonR = size.minDimension * 0.08f
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White.copy(alpha = 0.30f), Color.Transparent),
                center = moon,
                radius = moonR * 2.4f,
            ),
            radius = moonR * 2.4f,
            center = moon,
        )
        drawCircle(Color.White.copy(alpha = 0.85f), radius = moonR, center = moon)
        stars.forEach { s ->
            val twinkle = 0.35f + 0.45f * (0.5f + 0.5f * sin(time * s.speed + s.phase))
            drawCircle(
                color = Color.White.copy(alpha = twinkle),
                radius = s.r,
                center = Offset(s.x * size.width, s.y * size.height),
            )
        }
    }
}

// ---- Mist ---------------------------------------------------------------------

private class Band(val baseY: Float, val speed: Float, val phase: Float, val thickness: Float, val alpha: Float)

@Composable
private fun MistLayer(time: Float) {
    val bands = remember {
        val r = Random(7)
        List(5) {
            Band(
                baseY = 0.15f + it * 0.16f,
                speed = 0.02f + r.nextFloat() * 0.03f,
                phase = r.nextFloat(),
                thickness = 0.06f + r.nextFloat() * 0.05f,
                alpha = 0.06f + r.nextFloat() * 0.06f,
            )
        }
    }
    Canvas(Modifier.fillMaxSize()) {
        bands.forEach { b ->
            val drift = (frac(time * b.speed + b.phase) - 0.5f) * size.width * 0.4f
            val y = b.baseY * size.height
            val h = b.thickness * size.height
            drawCircle(
                color = Color.White.copy(alpha = b.alpha),
                radius = size.width * 0.9f,
                center = Offset(size.width * 0.5f + drift, y + h),
            )
        }
    }
}
