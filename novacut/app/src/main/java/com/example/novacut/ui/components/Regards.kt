package com.example.novacut.ui.home

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.statusBarsPadding
import com.example.novacut.ui.components.*
import com.example.novacut.ui.theme.novacutTheme
import androidx.compose.foundation.layout.Arrangement

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    val configuration = LocalConfiguration.current
    val squirclesHeight = (configuration.screenHeightDp * 0.3f).dp
    val SequenceHeight = (configuration.screenHeightDp * 0.2f).dp                
    val caucaPos = 
  
    var selectedTab by remember { mutableStateOf(HomeTab.EDITS) }
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xF2F0E6))
    ) {

@Composable
fun AnimatedSequence() {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(1000)
        )
    }

    Canvas(
        modifier = Modifier
      
        .fillMaxWidth()
    ) {
            val startX = SequenceHeight.width * 0.5f
            val startY = SequenceHeight.height * 0.0f

            val endX = RoraWide.size.width * 0.0f
            val endY = RoraWide.size.height / 2
       }

     Path().apply {
         moveTo(startX, startY)

    cubicTo(
        SequenceHeight.width * 0.6 , SequenceHeight.height * 0.3 ,
        SequenceHeight.width * 0.4 , SequenceHeight.height * 0.5 ,
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

@Composable
fun Jitters(modifier: Modifier = Modifier) {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(5000)
        )
    }

    val text = "spatial experiment"
    val words = text.split(" ")

    val jitters = remember {
        words[0].map {
            Offset(
                x = Random.nextFloat() * 10f - 5f,
                y = Random.nextFloat() * 10f - 5f
            )
        }
    }

    val jatters = remember {
        words[1].map {
            Offset(
                x = Random.nextFloat() * 10f - 5f,
                y = Random.nextFloat() * 10f - 5f
            )
        }
    }

    Column(modifier = modifier) {
        Row {
            words[0].forEachIndexed { index, char ->
                Text(
                    text = char.toString(),
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.offset(
                        x = (jitters[index].x * progress.value).dp,
                        y = (jitters[index].y * progress.value).dp
                    )
                )
            }
        }
        Row {
            words[1].forEachIndexed { index, char ->
                Text(
                    text = char.toString(),
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.offset(
                        x = (jatters[index].x * progress.value).dp,
                        y = (jatters[index].y * progress.value).dp
                    )
                )
            }
        }
    }
}
