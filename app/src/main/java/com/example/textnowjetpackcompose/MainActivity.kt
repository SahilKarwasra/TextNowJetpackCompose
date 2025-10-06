package com.example.textnowjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.textnowjetpackcompose.config.navigation.AppNavigation
import com.example.textnowjetpackcompose.ui.theme.TextNowJetpackComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TextNowJetpackComposeTheme {
                AppNavigation()
            }
        }
    }
}

