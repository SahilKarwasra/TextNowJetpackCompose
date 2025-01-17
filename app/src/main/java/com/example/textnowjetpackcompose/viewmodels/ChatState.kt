package com.example.textnowjetpackcompose.viewmodels

import com.example.textnowjetpackcompose.data.model.MessageModel
import com.example.textnowjetpackcompose.data.model.UserResponse

data class ChatState(
    val messages: List<MessageModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val users: List<UserResponse> = emptyList()
)