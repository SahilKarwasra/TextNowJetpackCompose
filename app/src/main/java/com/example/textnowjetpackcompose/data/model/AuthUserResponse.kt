package com.example.textnowjetpackcompose.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthUserResponse(
    @SerialName("_id") val id: String,
    val fullName: String,
    val email: String,
    val profilePic: String?
)
