package com.example.textnowjetpackcompose.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    val fullName: String,
    val email: String,
    val password: String
)