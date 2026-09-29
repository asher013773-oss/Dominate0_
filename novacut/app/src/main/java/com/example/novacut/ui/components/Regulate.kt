package com.example.novacut.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.Color

@Composable
fun AnimatedWord(
    word: String,
    startDelay: Long = 0L,
    modifier: Modifier = Modifier
) {
    val letters = remember(word) { word.toCharArray().toList() }
    val configuration = LocalConfiguration.current
    val positionX = (configuration.screenHeightDp * 0.5f).dp
    val positionY = (configuration.screenHeightDp * 0.3f).dp

    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(word, startDelay) {
        delay(startDelay)
        visible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "textAlpha"
    )

    Column(
        modifier = modifier
            .padding(end = 8.dp)
            .offset(x = positionX, y = positionY)
    ) {
        Row {
            letters.take(7).forEach { c ->
                Text(
                    text = c.toString(),
                    fontSize = 40.sp,
                    color = Color.Black
                )
            }
        }

        Row {
            Text(
                text = word,
                fontSize = 20.sp,
                color = Color.Black
            )
        }
    }
}
