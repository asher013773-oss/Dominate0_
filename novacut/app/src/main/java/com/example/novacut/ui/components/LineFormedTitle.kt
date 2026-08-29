package com.example.novacut.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.animation.core.VectorConverter

data class Seg(val start: Offset, val end: Offset)

val glyphs: Map<Char, List<Seg>> = mapOf(
    'A' to listOf(
        Seg(Offset(0.5f, 0f), Offset(0f, 1f)),
        Seg(Offset(0.5f, 0f), Offset(1f, 1f)),
        Seg(Offset(0f, 0.5f), Offset(1f, 0.5f))
    ),
    'u' to listOf(
        Seg(Offset(0f, 0.5f), Offset(0f, 1f)),
        Seg(Offset(0f, 1f), Offset(1f, 1f)),
        Seg(Offset(1f, 0.5f), Offset(1f, 1f))
    ),
    'r' to listOf(
        Seg(Offset(1f, 0.5f), Offset(0f, 0.5f)),
        Seg(Offset(0f, 0.5f), Offset(0f, 1f))
    ),
    'o' to listOf(
        Seg(Offset(0f, 0.5f), Offset(0f, 1f)),
        Seg(Offset(0f, 1f), Offset(1f, 1f)),
        Seg(Offset(1f, 1f), Offset(1f, 0.5f)),
        Seg(Offset(1f,0.5f), Offset(0f,0.5f))
    ),
    'a' to listOf(
        Seg(Offset(0f, 0.4f), Offset(0f, 0.7f)),
        Seg(Offset(0f, 0.4f), Offset(1f, 0.4f)),
        Seg(Offset(1f, 0.4f), Offset(1f, 0.7f)),
        Seg(Offset(1f, 0.7f), Offset(0f, 0.7f))    
    ),
    'R' to listOf(
        Seg(Offset(0f, 0f), Offset(0f, 1f)),
        Seg(Offset(0f, 0f), Offset(1f, 0f)),
        Seg(Offset(1f, 0f), Offset(1f, 0.5f)),
        Seg(Offset(0f, 0.5f), Offset(1f, 0.5f)),
        Seg(Offset(0f, 0.5f), Offset(1f, 1f))
    ),
    'e' to listOf(
        Seg(Offset(0f, 0.5f), Offset(0f, 1f)),
        Seg(Offset(0f, 1f), Offset(1f, 1f)),
        Seg(Offset(0.5f, 0.5f), Offset(0f, 1f)),
        Seg(Offset(0f, 0.5f), Offset(0.5f, 0.5f))
    ),
    'i' to listOf(
        Seg(Offset(0.5f, 0.5f), Offset(0.5f, 1f))
    ),
    'n' to listOf(
        Seg(Offset(0.2f, 1f), Offset(0.2f, 0.5f)),
        Seg(Offset(0.2f, 0.5f), Offset(1f, 0.5f)),
        Seg(Offset(1f, 0.5f), Offset(1f, 1f))
    ),
    'v' to listOf(
        Seg(Offset(0f, 0.5f), Offset(0.5f, 1f)),
        Seg(Offset(0.5f, 1f), Offset(1f, 0.5f))
    ),
     'g' to listOf(
        Seg(Offset(0.4f, 0f), Offset(1f, 0f)),
        Seg(Offset(1f, 0f), Offset(1f, 0.4f)),
        Seg(Offset(1f, 0.4f), Offset(0.4f, 0f)),
        Seg(Offset(1f, 0.4f), Offset(1f, 0.8f)),
        Seg(Offset(1f, 0.8f), Offset(0.7f, 0.8f))
    ),
   't' to listOf(
        Seg(Offset(0f, 0f), Offset(0f, 1f)),
        Seg(Offset(0f, 0f), Offset(1f, 0f)),
        Seg(Offset(1f, 0f), Offset(1f, 0.5f)),
        Seg(Offset(0f, 0.5f), Offset(1f, 0.5f)),
        Seg(Offset(0f, 0.5f), Offset(1f, 1f))
    )
)

class AnimatedSegment(
    val finalStart: Offset,
    val finalEnd: Offset,
    val delta: Animatable<Offset, AnimationVector2D>
)

fun buildWordSegments(
    text: String,
    topRightPx: Offset,
    letterSizePx: Float,
    letterSpacingPx: Float,
    spaceWidthPx: Float
): List<AnimatedSegment> {
    var totalWidth = 0f
    for (c in text) {
        totalWidth += if (c == ' ') spaceWidthPx else letterSizePx + letterSpacingPx
    }
    totalWidth -= letterSpacingPx

    val startX = topRightPx.x - totalWidth
    val startY = topRightPx.y
    val random = Random(System.currentTimeMillis())

    val segments = mutableListOf<AnimatedSegment>()
    var cursorX = startX

    for (c in text) {
        if (c == ' ') {
            cursorX += spaceWidthPx
            continue
        }
        val letterSegs = glyphs[c] ?: emptyList()
        for (seg in letterSegs) {
            val finalStart = Offset(
                cursorX + seg.start.x * letterSizePx,
                startY + seg.start.y * letterSizePx
            )
            val finalEnd = Offset(
                cursorX + seg.end.x * letterSizePx,
                startY + seg.end.y * letterSizePx
            )

            val angle = random.nextFloat() * 2f * PI.toFloat()
            val distance = 800f + random.nextFloat() * 400f
            val originDelta = Offset(cos(angle) * distance, sin(angle) * distance)

            segments.add(
                AnimatedSegment(
                    finalStart = finalStart,
                    finalEnd = finalEnd,
                    delta = Animatable(originDelta, Offset.VectorConverter)
                )
            )
        }
        cursorX += letterSizePx + letterSpacingPx
    }
    return segments
}

fun buildMultiLineSegments(
    lines: List<String>,
    topRightPx: Offset,
    letterSizePx: Float,
    letterSpacingPx: Float,
    spaceWidthPx: Float,
    lineHeightPx: Float
): List<AnimatedSegment> {
    val allSegments = mutableListOf<AnimatedSegment>()
    lines.forEachIndexed { lineIndex, line ->
        val lineTopRight = Offset(
            topRightPx.x,
            topRightPx.y + lineIndex * lineHeightPx
        )
        allSegments += buildWordSegments(
            text = line,
            topRightPx = lineTopRight,
            letterSizePx = letterSizePx,
            letterSpacingPx = letterSpacingPx,
            spaceWidthPx = spaceWidthPx
        )
    }
    return allSegments
}

@Composable
fun LineFormedTitle(
    lines: List<String> = listOf("Aurora", "Reinvigorate"),
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val density = LocalDensity.current
        val widthPx = with(density) { maxWidth.toPx() }
        val heightPx = with(density) { maxHeight.toPx() }

        val letterSizePx = with(density) { 23.dp.toPx() }
        val letterSpacingPx = with(density) { 8.dp.toPx() }
        val spaceWidthPx = with(density) { 20.dp.toPx() }
        val lineHeightPx = with(density) { 56.dp.toPx() }
        val paddingTopPx = with(density) { 48.dp.toPx() }
        val paddingRightPx = with(density) { 25.dp.toPx() }

        val topRightAnchor = remember(widthPx, heightPx) {
            Offset(widthPx - paddingRightPx, paddingTopPx)
        }

        val segments = remember(widthPx, heightPx, lines) {
            buildMultiLineSegments(
                lines = lines,
                topRightPx = topRightAnchor,
                letterSizePx = letterSizePx,
                letterSpacingPx = letterSpacingPx,
                spaceWidthPx = spaceWidthPx,
                lineHeightPx = lineHeightPx
            )
        }

        LaunchedEffect(segments) {
            segments.forEachIndexed { index, seg ->
                launch {
                    seg.delta.animateTo(
                        targetValue = Offset.Zero,
                        animationSpec = tween(
                            durationMillis = 850,
                            delayMillis = index * 20,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            }
        }

        Canvas(modifier = Modifier.fillMaxHeight(0.2f)) {
            segments.forEach { seg ->
                drawLine(
                    color = Color.White,
                    start = seg.finalStart + seg.delta.value,
                    end = seg.finalEnd + seg.delta.value,
                    strokeWidth = 4f,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}
