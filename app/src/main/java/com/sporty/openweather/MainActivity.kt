package com.sporty.openweather

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.sporty.openweather.core.ui.theme.OpenWeatherTheme
import com.sporty.openweather.navigation.OpenWeatherNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Transparent system bars with light (white) icons — the sky gradient bleeds
        // behind them, so the foreground must read against a dark backdrop.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )
        super.onCreate(savedInstanceState)
        setContent {
            OpenWeatherTheme {
                // No Scaffold: each screen paints its own full-bleed gradient and
                // insets its own content via WindowInsets.safeDrawing.
                OpenWeatherNavHost(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
