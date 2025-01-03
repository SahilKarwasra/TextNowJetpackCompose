package com.example.textnowjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.example.textnowjetpackcompose.navigation.AppNavigation
import com.example.textnowjetpackcompose.screens.auth.LoginScreen
import com.example.textnowjetpackcompose.screens.auth.SignUpScreen
import com.example.textnowjetpackcompose.ui.theme.TextNowJetpackComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TextNowJetpackComposeTheme {
                MainApp()
            }
        }
    }
}

