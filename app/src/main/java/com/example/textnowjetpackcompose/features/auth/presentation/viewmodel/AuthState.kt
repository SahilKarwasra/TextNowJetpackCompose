package com.example.textnowjetpackcompose.features.auth.presentation.viewmodel

import com.example.textnowjetpackcompose.features.home.domain.model.UserResponse

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Authenticated(val user: UserResponse) : AuthState()
    object UnAuthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}