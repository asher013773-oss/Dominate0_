package com.example.novacut

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.novacut.ui.HomeScreen
import com.example.novacut.ui.theme.novacutTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            novacutTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    HomeScreen()
                }
            }
        }
    }
}
