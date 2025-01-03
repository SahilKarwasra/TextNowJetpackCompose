package com.example.textnowjetpackcompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.textnowjetpackcompose.navigation.AppNavigation
import com.example.textnowjetpackcompose.screens.auth.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainApp(modifier: Modifier = Modifier) {
    val viewModel: AuthViewModel = koinViewModel()
    var isAuthChecked by remember { mutableStateOf(false) }
    var checkAuthCalled by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (!checkAuthCalled) {
            viewModel.checkAuth()
            isAuthChecked = true
            checkAuthCalled = true
        }
    }
    Scaffold {
        Surface(modifier = Modifier.padding(it)) {
            AppNavigation()
        }
    }
}