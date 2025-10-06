package com.example.textnowjetpackcompose.config.navigation

import kotlinx.serialization.Serializable

sealed class DestinationScreen{

    @Serializable
    data object SubGraphBottomBar : DestinationScreen()

    @Serializable
    data object SubGraphAuth : DestinationScreen()

    @Serializable
    object SubChatGraph : DestinationScreen()

    @Serializable
    data class ChatScreenObj(
        val userName: String,
        val userId: String,
        val receiverId: String
    ) : DestinationScreen()


    @Serializable
    data object SplashScreenObj : DestinationScreen()

    @Serializable
    data object LoadingScreenObj : DestinationScreen()

    @Serializable
    data object SignupScreenObj : DestinationScreen()

    @Serializable
    data object LoginScreenObj : DestinationScreen()

    @Serializable
    data object HomeScreenObj : DestinationScreen()

    @Serializable
    data object AiBotScreenObj : DestinationScreen()

    @Serializable
    data object ProfileScreenObj : DestinationScreen()


}