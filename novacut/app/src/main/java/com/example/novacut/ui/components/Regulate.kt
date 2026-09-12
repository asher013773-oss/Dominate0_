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

    Column(modifier = Modifier.padding(end = 8.dp)) {
    Row {
        letters.subList(0, 6).forEachIndexed { i, c ->
            Text(
                text = c.toString(),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .offset(x = offsets[i].value.dp)
                    .alpha(alphas[i].value)
            )
        }
    }
     
    Row {
        letters.subList(0, 6).forEachIndexed { i, c ->
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
