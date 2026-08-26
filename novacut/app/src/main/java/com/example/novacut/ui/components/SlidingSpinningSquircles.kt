package com.example.novacut.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.background
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.random.Random

// Fast-out, slow-in style curve: steep at the start, flattens toward the end.
private val FastThenSlow = CubicBezierEasing(0f, 0f, 0.2f, 1f)

private enum class SquircleSize(val dp: Int) {
    MEDIUM(56),
    LARGE(84)
}

private data class SquircleSpec(
    val size: SquircleSize,
    val restingXFraction: Float,   // 0f..1f position across the container width
    val restingYFraction: Float,   // 0f..1f position across the container height
    val startRotationDeg: Float,
    val restRotationDeg: Float,
    val startDelayMs: Long,
    val durationMs: Int,
    val color: Color
)

/**
 * Renders inside a container that occupies 30% of the screen width.
 * Five squircles (mixed medium/large) slide in from offscreen-left and
 * rotate into place with a fast-then-slow easing, staggered so they
 * don't all land at once.
 */
@Composable
fun SlidingSpinningSquircles(modifier: Modifier = Modifier) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth(0.3f)
            .fillMaxHeight()
    ) {
        val density = LocalDensity.current
        val containerWidthPx = with(density) { maxWidth.toPx() }
        val containerHeightPx = with(density) { maxHeight.toPx() }

        // Specs are created once and never regenerate on recomposition.
        val specs = remember {
            val palette = listOf(
                Color(0xFF000000)
                Color(0xFF000000),
                Color(0xFF000000),
                Color(0xFF000000),
                Color(0xFF000000)
            )
            val sizes = listOf(
                SquircleSize.LARGE,
                SquircleSize.MEDIUM,
                SquircleSize.LARGE,
                SquircleSize.MEDIUM,
                SquircleSize.MEDIUM
            )
            List(5) { i ->
                SquircleSpec(
                    size = sizes[i],
                    restingXFraction = 0.15f + i * 0.16f,
                    restingYFraction = 0.2f + Random.nextFloat() * 0.6f,
                    startRotationDeg = 180f + Random.nextFloat() * 90f,
                    restRotationDeg = Random.nextFloat() * 12f - 6f,
                    startDelayMs = i * 90L + Random.nextLong(0, 60),
                    durationMs = 800 + Random.nextInt(-80, 120),
                    color = palette[i]
                )
            }
        }

        specs.forEach { spec ->
            AnimatedSquircle(
                spec = spec,
                containerWidthPx = containerWidthPx,
                containerHeightPx = containerHeightPx
            )
        }
    }
}

@Composable
private fun AnimatedSquircle(
    spec: SquircleSpec,
    containerWidthPx: Float,
    containerHeightPx: Float
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(spec) {
        delay(spec.startDelayMs)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = spec.durationMs,
                easing = FastThenSlow
            )
        )
    }

    val density = LocalDensity.current
    val sizePx = with(density) { spec.size.dp.dp.toPx() }

    val restingXPx = containerWidthPx * spec.restingXFraction
    val restingYPx = containerHeightPx * spec.restingYFraction
    // Fully offscreen to the left: resting X minus its own width minus a margin.
    val offscreenXPx = -sizePx - 40f

    val eased = progress.value // easing already baked into the tween
    val currentX = offscreenXPx + (restingXPx - offscreenXPx) * eased
    val currentRotation = spec.startRotationDeg +
        (spec.restRotationDeg - spec.startRotationDeg) * eased

    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .size(spec.size.dp.dp)
            .graphicsLayer {
                translationX = currentX
                translationY = restingYPx
                rotationZ = currentRotation
                alpha = 0.15f + 0.15f * eased // subtle fade-in alongside the motion
            }
            .clip(RoundedCornerShape(30)) // squircle approximation
            .background(spec.color)
    )
}
