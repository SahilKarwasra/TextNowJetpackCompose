package com.example.textnowjetpackcompose.features.auth.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.textnowjetpackcompose.R
import com.example.textnowjetpackcompose.features.auth.presentation.viewmodel.AuthState
import com.example.textnowjetpackcompose.features.auth.presentation.viewmodel.AuthViewModel
import com.example.textnowjetpackcompose.config.navigation.DestinationScreen

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel,
    navigate: (DestinationScreen) -> Unit
) {

    val isDarkMode = isSystemInDarkTheme()
    val imageResource = if (isDarkMode) {
        R.drawable.textnow
    } else {
        R.drawable.textnow1
    }

    val state by viewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        when (state) {
            is AuthState.Authenticated -> {
                navigate(DestinationScreen.SubGraphBottomBar)
            }
            is AuthState.UnAuthenticated -> {
                navigate(DestinationScreen.LoginScreenObj)
            }
            else -> {}
        }
    }



    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = null,
            modifier = Modifier.padding(bottom = 20.dp)
        )
    }


}