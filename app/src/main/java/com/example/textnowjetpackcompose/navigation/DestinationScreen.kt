package com.example.textnowjetpackcompose.navigation

import kotlinx.serialization.Serializable

sealed class DestinationScreen{

    @Serializable
    data object LoadingScreenObj : DestinationScreen()

    @Serializable
    data object SignupScreenObj : DestinationScreen()

    @Serializable
    data object LoginScreenObj : DestinationScreen()

    @Serializable
    data object HomeScreenObj : DestinationScreen()

    @Serializable
    data object StatusScreenObj : DestinationScreen()

    @Serializable
    data object ProfileScreenObj : DestinationScreen()

    @Serializable
    data class ChatScreenObj(
        val userId: String,
        val userName: String
    ) : DestinationScreen()


}