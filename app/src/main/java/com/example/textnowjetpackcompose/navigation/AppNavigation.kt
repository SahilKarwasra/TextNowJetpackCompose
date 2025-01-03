package com.example.textnowjetpackcompose.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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



    NavHost(
        navController = navController,
        startDestination = if (viewModel.isAuthenticated) DestinationScreen.HomeScreenObj
        else DestinationScreen.SignupScreenObj
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
