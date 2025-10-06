package com.example.textnowjetpackcompose.features.home.presentation.viewmodel

import com.example.textnowjetpackcompose.features.home.domain.model.UserResponse

sealed class HomeScreenState {
    object Empty : HomeScreenState()
    object Loading : HomeScreenState()
    data class Success(val messages: List<UserResponse>) : HomeScreenState()
    data class Error(val message: String) : HomeScreenState()
}