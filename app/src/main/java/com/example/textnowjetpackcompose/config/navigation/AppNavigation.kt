package com.example.textnowjetpackcompose.config.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.textnowjetpackcompose.config.SocketHandler
import com.example.textnowjetpackcompose.features.auth.presentation.screen.LoginScreen
import com.example.textnowjetpackcompose.features.auth.presentation.screen.SignUpScreen
import com.example.textnowjetpackcompose.features.auth.presentation.screen.SplashScreen
import com.example.textnowjetpackcompose.features.auth.presentation.viewmodel.AuthViewModel
import com.example.textnowjetpackcompose.features.home.presentation.screen.HomeScreen
import com.example.textnowjetpackcompose.config.navigation.NavigationAnims.enterTransition
import com.example.textnowjetpackcompose.config.navigation.NavigationAnims.exitTransition
import com.example.textnowjetpackcompose.config.navigation.NavigationAnims.popEnterTransition
import com.example.textnowjetpackcompose.config.navigation.NavigationAnims.popExitTransition
import com.example.textnowjetpackcompose.features.chat.presentation.components.ChatScreenTopBar
import com.example.textnowjetpackcompose.features.chat.presentation.components.SendMessageTextField
import com.example.textnowjetpackcompose.features.chat.presentation.screen.ChatScreen
import com.example.textnowjetpackcompose.features.chat.presentation.viewmodel.ChatViewModel
import com.example.textnowjetpackcompose.features.home.presentation.viewmodel.HomeScreenViewModel
import com.example.textnowjetpackcompose.features.profile.presentation.screen.ProfileScreen
import com.example.textnowjetpackcompose.features.shared.BottomNavigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val snackbarHostState = remember { SnackbarHostState() }
    val snackBarCoroutineScope = rememberCoroutineScope()

    fun shouldShowBottomBar(navBackStackEntry: NavBackStackEntry): Boolean {
        val currentDestination = navBackStackEntry.destination
        return currentDestination.hierarchy.any() {
            it.hasRoute(DestinationScreen.SubGraphBottomBar::class)
        }
    }

    fun shouldShowBottomBarFlow(): Flow<Boolean> {
        return navController.currentBackStackEntryFlow.map { backStackEntry ->
            shouldShowBottomBar(backStackEntry)
        }
    }

    fun isChatScreen(navBackStackEntry: NavBackStackEntry): Boolean {
        val currentDestination = navBackStackEntry.destination
        return currentDestination.hierarchy.any() {
            it.hasRoute(DestinationScreen.ChatScreenObj::class)
        }
    }

    fun isChatScreenFlow(): Flow<Boolean> {
        return navController.currentBackStackEntryFlow.map { backStackEntry ->
            isChatScreen(backStackEntry)
        }
    }

    val showBottomBar by shouldShowBottomBarFlow().collectAsState(initial = false)
    val isChatScreenOrNot by isChatScreenFlow().collectAsState(initial = false)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val chatTitle: String? = if (
        navBackStackEntry?.destination?.hasRoute(DestinationScreen.ChatScreenObj::class) == true
    ) {
        navBackStackEntry?.toRoute<DestinationScreen.ChatScreenObj>()?.userName
    } else {
        null
    }
//    val chatId: String? = if (
//        navBackStackEntry?.destination?.hasRoute(DestinationScreen.ChatScreenObj::class) == true
//    ) {
//        navBackStackEntry?.toRoute<DestinationScreen.ChatScreenObj>()?.userId
//    } else {
//        null
//    }
//    val receiverId: String? = if (
//        navBackStackEntry?.destination?.hasRoute(DestinationScreen.ChatScreenObj::class) == true
//    ) {
//        navBackStackEntry?.toRoute<DestinationScreen.ChatScreenObj>()?.receiverId
//    } else {
//        null
//    }
    Scaffold(
        topBar = {
            if (isChatScreenOrNot && chatTitle != null) {
                ChatScreenTopBar(
                    chatTitle,
                    navController::navigateUp,
                    { /*TODO*/ }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigation(
                    navController = navController,
                    navigate = navController::navigate
                )
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = DestinationScreen.SubGraphAuth,
            modifier = Modifier.padding()
        ) {

            authenticationGraph(
                appController = navController,
                snackbarHostState = snackbarHostState,
                coroutineScope = snackBarCoroutineScope
            )

            bottomBarGraph(
                navController,
                snackbarHostState,
                snackBarCoroutineScope
            )

            chatGraph(navController)

        }
    }
}


fun NavGraphBuilder.bottomBarGraph(
    appController: NavController,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
) {
    navigation<DestinationScreen.SubGraphBottomBar>(
        startDestination = DestinationScreen.HomeScreenObj,
    ) {
        composable<DestinationScreen.HomeScreenObj>(
            enterTransition = { fadeIn() + expandHorizontally() },
            exitTransition = { fadeOut() + shrinkHorizontally() },
            popEnterTransition = { fadeIn() + expandHorizontally() },
            popExitTransition = { fadeOut() + shrinkHorizontally() }
        ) {
            val homeViewModel : HomeScreenViewModel = it.sharedKoinViewModel(appController)
            val chatViewModel : ChatViewModel = it.sharedKoinViewModel(appController)
            val authViewModel : AuthViewModel = it.sharedKoinViewModel(appController)
            HomeScreen(
                navigate = {
                    appController.navigate(it)
                },
                homeViewModel = homeViewModel,
                chatViewModel = chatViewModel,
                authViewModel = authViewModel
            )

        }
        composable<DestinationScreen.AiBotScreenObj> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("ChatBot")
            }
        }
        composable<DestinationScreen.ProfileScreenObj> {
            val authViewModel: AuthViewModel = it.sharedKoinViewModel(appController)
            ProfileScreen(
                authViewModel,
                navigate = {
                    appController.navigate(it) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                snackbarHostState = snackbarHostState,
                coroutineScope = coroutineScope
            )
        }
    }
}

fun NavGraphBuilder.authenticationGraph(
    appController: NavController,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    navigation<DestinationScreen.SubGraphAuth>(
        startDestination = DestinationScreen.SplashScreenObj,
    ) {
        composable<DestinationScreen.SplashScreenObj> {
            val authViewModel: AuthViewModel = it.sharedKoinViewModel(appController)

            SplashScreen(
                navigate = {
                    appController.navigate(it) {
                        popUpTo(0) { inclusive = true }
                    }
                }, viewModel = authViewModel
            )
        }
        composable<DestinationScreen.SignupScreenObj>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition },
        ) {
            val authViewModel: AuthViewModel = it.sharedKoinViewModel(appController)

            SignUpScreen(
                navigate = {
                    appController.navigate(it) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                viewModel = authViewModel,
                snackbarHostState = snackbarHostState,
                coroutineScope = coroutineScope
            )
        }
        composable<DestinationScreen.LoginScreenObj>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition },
        ) {
            val authViewModel: AuthViewModel = it.sharedKoinViewModel(appController)

            LoginScreen(
                navigate = {
                    appController.navigate(it) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                viewModel = authViewModel,
                snackbarHostState = snackbarHostState,
                coroutineScope = coroutineScope
            )
        }
    }
}

fun NavGraphBuilder.chatGraph(appController: NavController) {
    navigation<DestinationScreen.SubChatGraph>(
        startDestination = DestinationScreen.ChatScreenObj::class
    ) {
        composable<DestinationScreen.ChatScreenObj>(
            enterTransition = { fadeIn() + expandHorizontally() },
            exitTransition = { fadeOut() + shrinkHorizontally() },
            popEnterTransition = { fadeIn() + expandHorizontally() },
            popExitTransition = { fadeOut() + shrinkHorizontally() },
        ) {
            val args = it.toRoute<DestinationScreen.ChatScreenObj>()
            val chatViewModel: ChatViewModel = it.sharedKoinViewModel(appController)
            ChatScreen(
                userId = args.userId,
                viewModel = chatViewModel,
                receiverId = args.receiverId,
            )
        }
    }
}




@Composable
private inline fun <reified T: ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel<T>()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(
        viewModelStoreOwner = parentEntry
    )
}