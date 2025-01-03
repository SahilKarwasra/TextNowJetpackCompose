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
    var appState by remember { mutableStateOf<AppState>(AppState.Loading) }

    LaunchedEffect(Unit) {
        appState = try {
            val authResult = viewModel.checkAuth()
            authResult.fold(
                onSuccess = {
                    Log.e("AppNavigation", "Authentication Successfully")
                    AppState.Authenticated
                },
                onFailure = { error ->
                    Log.e("AppNavigation", "Authentication check failed", error)
                    AppState.Unauthenticated
                }
            )
        } catch (e: Exception) {
            Log.e("AppNavigation", "Unexpected error during auth check", e)
            AppState.Unauthenticated
        }
    }

    // Navigate based on app state
    LaunchedEffect(appState) {
        when (appState) {
            AppState.Authenticated -> {
                navController.navigate(DestinationScreen.HomeScreenObj) {
                    popUpTo(0) { inclusive = true }
                }
            }
            AppState.Unauthenticated -> {
                navController.navigate(DestinationScreen.SignupScreenObj) {
                    popUpTo(0) { inclusive = true }
                }
            }
            AppState.Loading -> {
                // Do nothing, keep showing loading screen
            }
        }
    }


    NavHost(
        navController = navController,
        startDestination = DestinationScreen.LoadingScreenObj
    ) {
        composable<DestinationScreen.LoadingScreenObj> {
            LoadingScreen()
        }
        composable<DestinationScreen.SignupScreenObj> {
            SignUpScreen(navigate = {
                navController.navigate(it)
            })
        }
        composable<DestinationScreen.LoginScreenObj> {
            LoginScreen(navigate = {
                navController.navigate(it)
            })
        }
        composable<DestinationScreen.HomeScreenObj> {
            HomeScreen()
        }
    }
}

sealed class AppState {
    object Loading : AppState()
    object Authenticated : AppState()
    object Unauthenticated : AppState()
}