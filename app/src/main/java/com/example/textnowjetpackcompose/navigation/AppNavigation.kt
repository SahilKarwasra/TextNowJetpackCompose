package com.example.textnowjetpackcompose.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.textnowjetpackcompose.screens.auth.LoadingScreen
import com.example.textnowjetpackcompose.screens.auth.LoginScreen
import com.example.textnowjetpackcompose.screens.auth.SignUpScreen
import com.example.textnowjetpackcompose.screens.chats.ChatScreen
import com.example.textnowjetpackcompose.screens.home.HomeScreen
import com.example.textnowjetpackcompose.screens.profile.ProfileScreen
import com.example.textnowjetpackcompose.screens.aibot.AiBotScreen
import com.example.textnowjetpackcompose.screens.utils.BottomNavigationBar
import com.example.textnowjetpackcompose.viewmodels.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
            modifier = Modifier.padding(
                horizontal = innerPadding.calculateLeftPadding(
                    LayoutDirection.Ltr
                )
            )
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
                    }
                )
            }
            composable<DestinationScreen.AiBotScreenObj> {
                showBottomBar.value = true
                AiBotScreen()
            }
            composable<DestinationScreen.ProfileScreenObj> {
                showBottomBar.value = true
                ProfileScreen(navigate = {
                    navController.navigate(it)
                })
            }
            composable<DestinationScreen.ChatScreenObj> {
                showBottomBar.value = false
                val fullName = it.toRoute<DestinationScreen.ChatScreenObj>().userName
                val userId = it.toRoute<DestinationScreen.ChatScreenObj>().userId
                ChatScreen(
                    receiverName = fullName,
                    userId = userId,
                    navigateToHomeScreen = {
                        navController.navigate(it)
                    }
                )
            }
        }
    }
}
