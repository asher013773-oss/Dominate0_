package com.example.novacut.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateTo
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.withFrameNanos
import androidx.compose.runtime.remember
import androidx.compose.foundation.Canvas
import kotlin.random.Random
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.draw.clip
import kotlinx.coroutines.delay
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.animation.core.Spring
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.sp

@Composable
fun AnimatedWord(
    word: String,
    startDelay: Long = 0L,
    modifier: Modifier = Modifier
) {
    val letters = remember(word) { word.toCharArray().toList() }
    val offsets = remember(word) { letters.map { Animatable(300f) } }
    val alphas = remember(word) { letters.map { Animatable(0f) } }
    val configuration = LocalConfiguration.current
    val positionx = (configuration.screenHeightDp * 0.5f).dp
    val positiony = (configuration.screenHeightDp * 0.3f).dp

    LaunchedEffect(word) {
        letters.subList(7, 19).forEachIndexed { i, c ->
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

    Column(
    modifier = Modifier
        .padding(end = 8.dp)
        .offset(x = positionx, y = positiony)
) {
    Row {
        letters.take(6).forEachIndexed { i, c ->
            Text(
                text = c.toString(),
                fontSize = 40.dp,
                modifier = Modifier
                    .offset(x = offsets[i].value.dp)
                    .alpha(alphas[i].value)
            )
        }
    }
    
    Row {
        letters.subList(7, 19).forEachIndexed { i, c ->
            Text(
                text = c.toString(),
                fontSize = 20.dp,
                modifier = Modifier
                    .offset(x = offsets[i].value.dp)
                    .alpha(alphas[i].value)
                )
            }
        }
    }
}
