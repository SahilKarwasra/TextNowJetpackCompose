package com.example.textnowjetpackcompose.features.auth.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    val fullName: String,
    val email: String,
    val password: String
)