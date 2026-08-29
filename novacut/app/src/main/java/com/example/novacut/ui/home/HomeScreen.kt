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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.statusBarsPadding
import com.example.novacut.ui.components.*
import com.example.novacut.ui.theme.novacutTheme


@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    var squirclesHeightPx by remember { mutableStateOf(0)
    val infiniteTransition = rememberInfiniteTransition(label = "ColorLoop")

    val blendedColor by infiniteTransition.animateColor(
        initialValue = Color(0xFFD9F4DA),
        targetValue = Color(0xFFFFFFFF),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ColorBlend"
    )

    var selectedTab by remember { mutableStateOf(HomeTab.EDITS) }
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(blendedColor)
            .onGloballyPositioned { coords ->
                    squirclesHeightPx = coords.size.height
    ) {
        
        Box(modifier = Modifier.fillMaxSize()) {
            LineFormedTitle()
            SlidingSpinningSquircles()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = with(LocalDensity.current) { squirclesHeightPx.toDp() })
                .verticalScroll(rememberScrollState())
        ) {
            HomeTabRow(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    }
}
