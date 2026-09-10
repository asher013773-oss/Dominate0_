package com.example.novacut.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Reveals [word] one letter at a time: each letter springs in from an x-offset
 * while fading in, staggered by 40ms per letter, starting after [startDelay].
 */
@Composable
fun AnimatedWord(
    word: String,
    startDelay: Long = 0L,
    modifier: Modifier = Modifier
) {
    val letters = remember(word) { word.toCharArray().toList() }
    val offsets = remember(word) { letters.map { Animatable(300f) } }
    val alphas = remember(word) { letters.map { Animatable(0f) } }

    LaunchedEffect(word) {
        letters.indices.forEach { i ->
            launch {
                delay(startDelay + i * 40L)
                offsets[i].animateTo(
                    targetValue = 0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
            launch {
                delay(startDelay + i * 40L)
                alphas[i].animateTo(1f, tween(300))
            }
        }
    }

    Row(modifier = modifier.padding(end = 8.dp)) {
        letters.forEachIndexed { i, c ->
            Text(
                text = c.toString(),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .offset(x = offsets[i].value.dp)
                    .alpha(alphas[i].value)
            )
        }
    }
}

/**
 * Draws a curved stroke that animates in over 1s, from the top-center of its
 * bounds down to a point near the bottom-right. Uses the Canvas's own
 * DrawScope size instead of an undefined external size reference.
 */
@Composable
fun AnimatedSequence(modifier: Modifier = Modifier) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(targetValue = 1f, animationSpec = tween(1000))
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        val startX = size.width * 0.5f
        val startY = 0f
        val endX = size.width * 0.8f
        val endY = size.height * 0.8f

        val path = Path().apply {
            moveTo(startX, startY)
            cubicTo(
                size.width * 0.6f, size.height * 0.3f,
                size.width * 0.4f, size.height * 0.5f,
                endX, endY
            )
        }

        val pathMeasure = PathMeasure()
        pathMeasure.setPath(path, false)
        val animatedPath = Path()
        pathMeasure.getSegment(
            0f,
            pathMeasure.length * progress.value,
            animatedPath,
            true
        )

        drawPath(
            path = animatedPath,
            color = Color.Black,
            style = Stroke(width = 8f)
        )
    }
}

/**
 * Renders "spatial experiment" with each letter jittering into place from a
 * random offset over 5s. Generalized over an arbitrary word list rather than
 * hard-indexing words[0]/words[1], so it won't crash if the text changes.
 */
@Composable
fun Jitterh(modifier: Modifier = Modifier) {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        progress.animateTo(targetValue = 1f, animationSpec = tween(5000))
    }

    val text = "spatial experiment"
    val words = remember(text) { text.split(" ") }

    val jitterOffsets = remember {
        words.map { w ->
            w.map {
                Offset(
                    x = Random.nextFloat() * 10f - 5f,
                    y = Random.nextFloat() * 10f - 5f
                )
            }
        }
    }

    // caucaPos in the original was typed as Dp and had no .x/.y — this was
    // presumably meant as a single vertical offset for the block of text.
    val topOffset = (LocalConfiguration.current.screenHeightDp * 0.3f).dp

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.offset(y = topOffset)) {
            words.forEachIndexed { wIndex, w ->
                Row {
                    w.forEachIndexed { cIndex, c ->
                        Text(
                            text = c.toString(),
                            style = MaterialTheme.typography.displaySmall,
                            modifier = Modifier.offset(
                                x = (jitterOffsets[wIndex][cIndex].x * progress.value).dp,
                                y = (jitterOffsets[wIndex][cIndex].y * progress.value).dp
                            )
                        )
                    }
                }
            }
        }
    }
}

/**
 * Orchestrates the full title sequence: both words letter-reveal immediately,
 * then after 248ms the curved line animates in, then 1s after that the
 * jittering subtext appears. Sequencing is done via a phase counter advanced
 * in a single LaunchedEffect — composables can't be called after delay()
 * as a bare imperative sequence outside a composable body.
 */
@Composable
fun AnimatedTitleSequence(
    word1: String = "Aurora",
    word2: String = "Reinvigorate",
    modifier: Modifier = Modifier
) {
    var phase by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        delay(248L)
        phase = 1
        delay(1000L)
        phase = 2
    }

    Column(modifier = modifier) {
        AnimatedWord(word = word1)
        AnimatedWord(word = word2, startDelay = word1.length * 40L + 200L)
        if (phase >= 1) AnimatedSequence()
        if (phase >= 2) Jitterh()
    }
}
