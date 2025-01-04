package com.example.textnowjetpackcompose.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.textnowjetpackcompose.viewmodels.AuthViewModel
import com.example.textnowjetpackcompose.screens.auth.LoadingScreen
import com.example.textnowjetpackcompose.screens.auth.LoginScreen
import com.example.textnowjetpackcompose.screens.auth.SignUpScreen
import com.example.textnowjetpackcompose.screens.home.HomeScreen
import com.example.textnowjetpackcompose.screens.profile.ProfileScreen
import com.example.textnowjetpackcompose.screens.status.StatusScreen
import com.example.textnowjetpackcompose.screens.utils.BottomNavigationBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: AuthViewModel = koinViewModel()

    val isAuthenticated = viewModel.isAuthenticated
    val isLoading = viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.checkAuth()
    }

    val showBottomBar = remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            if (showBottomBar.value) {
                BottomNavigationBar(navigate = {
                    navController.navigate(it) {
                        popUpTo(0) { inclusive = true }
                    }
                })
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (isLoading.value) DestinationScreen.LoadingScreenObj else {
                if (isAuthenticated) DestinationScreen.HomeScreenObj else DestinationScreen.SignupScreenObj
            },

            ) {
            composable<DestinationScreen.LoadingScreenObj> {
                showBottomBar.value = false
                LoadingScreen()
            }
            composable<DestinationScreen.SignupScreenObj> {
                showBottomBar.value = false
                SignUpScreen(navigate = {
                    navController.navigate(it) {
                        popUpTo(0) { inclusive = true }
                    }
                })
            }
            composable<DestinationScreen.LoginScreenObj> {
                showBottomBar.value = false
                LoginScreen(navigate = {
                    navController.navigate(it) {
                        popUpTo(0) { inclusive = true }
                    }
                })
            }
            composable<DestinationScreen.HomeScreenObj> {
                showBottomBar.value = true
                HomeScreen(
                    navigate = {
                        navController.navigate(it)
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            composable<DestinationScreen.StatusScreenObj> {
                showBottomBar.value = true
                StatusScreen()
            }
            composable<DestinationScreen.ProfileScreenObj> {
                showBottomBar.value = true
                ProfileScreen(navigate = {
                    navController.navigate(it)
                })
            }
        }
    }
}
