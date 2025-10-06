package com.example.textnowjetpackcompose.features.home.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName("_id") val id: String,
    val fullName: String,
    val email: String,
    val profilePic: String?,
    val lastMessage: LastMessage? = null
)

@Serializable
data class LastMessage(
    val text: String,
    val createdAt: String
)
