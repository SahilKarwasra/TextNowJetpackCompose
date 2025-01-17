package com.example.textnowjetpackcompose.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MessageModel(
    val senderId: String,
    val receiverId: String,
    val text: String? = null,
    val image: String? = null,
    val createdAt: String,
    val updatedAt: String? = null
)