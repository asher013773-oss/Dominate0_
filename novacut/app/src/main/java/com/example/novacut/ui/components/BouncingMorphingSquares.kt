package com.example.novacut.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.random.Random
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope

private const val MAX_LEVEL = 2

private class SquareState(
    startX: Float,
    startY: Float,
    val sizePx: Float,
    val color: Color
) {
    val offsetX = Animatable(startX)
    val offsetY = Animatable(startY)
    val cornerPercent = Animatable(0f) // 0 = square, 50 = circle
    var level = 0
    var touching = false
}

@Composable
fun BouncingMorphingSquares() {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val maxWidthPx = with(LocalDensity.current) { maxWidth.toPx() }
        val maxHeightPx = with(LocalDensity.current) { maxHeight.toPx() }
        val density = LocalDensity.current

        val squares = remember {
            listOf(
                SquareState(0f, 0f, with(density) { 50.dp.toPx() }, Color.Black),
                SquareState(0f, 0f, with(density) { 45.dp.toPx() }, Color.Black),
                SquareState(0f, 0f, with(density) { 50.dp.toPx() }, Color.Black),
                SquareState(0f, 0f, with(density) { 30.dp.toPx() }, Color.Black),
                SquareState(0f, 0f, with(density) { 25.dp.toPx() }, Color.Black)
            ).onEach {
                it.offsetX.updateBounds(0f, maxWidthPx - it.sizePx)
                it.offsetY.updateBounds(0f, maxHeightPx - it.sizePx)
            }
        }

        squares.forEachIndexed { index, square ->
            LaunchedEffect(square) {
                delay(Random.nextLong(0, 500))
                while (true) {
                    val targetX = Random.nextFloat() * (maxWidthPx - square.sizePx)
                    val targetY = Random.nextFloat() * (maxHeightPx - square.sizePx)
                    val duration = 1200 + index * 300

                    launch { square.offsetX.animateTo(targetX, tween(duration, easing = FastOutSlowInEasing)) }
                    launch { square.offsetY.animateTo(targetY, tween(duration, easing = FastOutSlowInEasing)) }
                    delay(duration.toLong())
                }
            }
        }

        LaunchedEffect(squares) {
            while (true) {
                withFrameNanos {
                    for (i in squares.indices) {
                        for (j in i + 1 until squares.size) {
                            val a = squares[i]
                            val b = squares[j]

                            val centerAx = a.offsetX.value + a.sizePx / 2f
                            val centerAy = a.offsetY.value + a.sizePx / 2f
                            val centerBx = b.offsetX.value + b.sizePx / 2f
                            val centerBy = b.offsetY.value + b.sizePx / 2f

                            val dx = centerAx - centerBx
                            val dy = centerAy - centerBy
                            val distance = sqrt(dx * dx + dy * dy)
                            val collisionThreshold = (a.sizePx + b.sizePx) / 2f

                            val isOverlapping = distance < collisionThreshold

                            if (isOverlapping && !a.touching && !b.touching) {
                                a.touching = true
                                b.touching = true
                                bumpShape(a)
                                bumpShape(b)
                            } else if (!isOverlapping) {
                                a.touching = false
                                b.touching = false
                            }
                        }
                    }
                }
            }
        }

        squares.forEach { square ->
            val shape = remember {
                derivedStateOf { RoundedCornerShape(percent = square.cornerPercent.value.roundToInt()) }
            }
            Box(
                modifier = Modifier
                    .offset { IntOffset(square.offsetX.value.roundToInt(), square.offsetY.value.roundToInt()) }
                    .size(with(LocalDensity.current) { square.sizePx.toDp() })
                    .background(square.color, shape = shape.value)
            )
        }
    }
}

private fun CoroutineScope.bumpShape(square: SquareState) {
    if (square.level >= MAX_LEVEL) return
    square.level++
    val targetPercent = when (square.level) {
        1 -> 25f  // squircle
        2 -> 50f  // circle
        else -> 0f
    }
    launch {
        square.cornerPercent.animateTo(targetPercent, tween(400, easing = FastOutSlowInEasing))
    }
}
