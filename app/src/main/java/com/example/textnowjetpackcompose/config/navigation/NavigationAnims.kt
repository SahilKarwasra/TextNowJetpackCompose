package com.example.textnowjetpackcompose.config.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

object NavigationAnims {
    val enterTransition = slideInHorizontally(
        tween(
            durationMillis = 500,
            delayMillis = 100,
        )
    ) { -it }
    val exitTransition = slideOutHorizontally(
        tween(
            durationMillis = 500,
            delayMillis = 100,
        )
    ) { it }
    val popEnterTransition = slideInHorizontally(
        tween(
            durationMillis = 500,
            delayMillis = 100,
        )
    ) { -it }
    val popExitTransition = slideOutHorizontally(
        tween(
            durationMillis = 500,
            delayMillis = 100,
        )
    ) { -it }
}