package com.example.novacut.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.material3.MaterialTheme

@Composable
fun AnimatedWord(
    word: String,
    startDelay: Long = 0L,
    modifier: Modifier = Modifier
) {
    val letters = word.toCharArray()
    val offsets = remember { letters.map { Animatable(300f) } }
    val alphas = remember { letters.map { Animatable(0f) } }

    LaunchedEffect(word) {
        for (i in letters.indices) {
            launch {
                kotlinx.coroutines.delay(startDelay + i * 40L)
                offsets[i].animateTo(
                    targetValue = 0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
            launch {
                kotlinx.coroutines.delay(startDelay + i * 40L)
                alphas[i].animateTo(1f, tween(300))
            }
        }
    }

    Row(modifier = modifier.padding(end = 8.dp)) {
        letters.forEachIndexed { i, c ->
            androidx.compose.material3.Text(
                text = c.toString(),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .offset(x = offsets[i].value.dp)
                    .alpha(alphas[i].value)
            )
        }
    }
}

@Composable
fun AnimatedTwoWords(
    word1: String = "Aurora",
    word2: String = "Reinvigorate",
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        AnimatedWord(word = word1)
        AnimatedWord(
            word = word2,
            startDelay = word1.length * 40L + 200L
        )
    }
}
