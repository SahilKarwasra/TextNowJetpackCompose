package com.example.textnowjetpackcompose.features.chat.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class MessageRequest(
    val senderId: String,
    val receiverId: String,
    val text: String? = null,
    val image: String? = null,
    val createdAt: String,
    val updatedAt: String? = null
)

@Serializable
data class MessageModel(
    val _id: String,
    val senderId: String,
    val receiverId: String,
    val text: String? = null,
    val image: String? = null,
    val createdAt: String,
    val updatedAt: String? = null
)