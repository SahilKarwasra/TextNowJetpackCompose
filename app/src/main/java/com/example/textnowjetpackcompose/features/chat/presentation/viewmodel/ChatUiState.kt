package com.example.textnowjetpackcompose.features.chat.presentation.viewmodel

import com.example.textnowjetpackcompose.features.chat.domain.model.MessageModel

data class ChatUiState(
    val messages: List<MessageModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isConnected: Boolean = false,
    val isSendingMessage: Boolean = false,
    val currentReceiverId: String? = null
)