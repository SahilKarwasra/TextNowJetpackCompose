package com.example.textnowjetpackcompose.features.chat.presentation.viewmodel

sealed class ChatEvent {
    data class ShowError(val message: String) : ChatEvent()
    object MessageSent : ChatEvent()
    object ScrollToBottom : ChatEvent()
}