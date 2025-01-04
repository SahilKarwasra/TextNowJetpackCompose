package com.example.textnowjetpackcompose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.textnowjetpackcompose.screens.auth.AuthViewModel
import com.example.textnowjetpackcompose.screens.auth.LoadingScreen
import com.example.textnowjetpackcompose.screens.auth.LoginScreen
import com.example.textnowjetpackcompose.screens.auth.SignUpScreen
import com.example.textnowjetpackcompose.screens.home.HomeScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: AuthViewModel = koinViewModel()

    val isAuthenticated = viewModel.isAuthenticated
    val isLoading = viewModel.isLoading.collectAsState()

    // Perform authentication check on navigation start
    LaunchedEffect(Unit) {
        viewModel.checkAuth()
    }
    NavHost(
        navController = navController,
        startDestination = if (isLoading.value) DestinationScreen.LoadingScreenObj else {
            if (isAuthenticated) DestinationScreen.HomeScreenObj else DestinationScreen.SignupScreenObj
        }
    ) {
        composable<DestinationScreen.LoadingScreenObj> {
            LoadingScreen()
        }
        composable<DestinationScreen.SignupScreenObj> {
            SignUpScreen(navigate = {
                navController.navigate(it) {
                    popUpTo(0) { inclusive = true }
                }
            })
        }
        composable<DestinationScreen.LoginScreenObj> {
            LoginScreen(navigate = {
                navController.navigate(it) {
                    popUpTo(0) { inclusive = true }
                }
            })
        }
        composable<DestinationScreen.HomeScreenObj> {
            HomeScreen()
        }
    }
}
