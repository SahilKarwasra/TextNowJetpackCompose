package com.example.textnowjetpackcompose.features.chat.domain.repo

import com.example.textnowjetpackcompose.features.chat.domain.model.MessageModel

interface ChatRepo {
    suspend fun getMessages(receiverId: String): Result<List<MessageModel>>
    suspend fun sendMessage(receiverId: String, messageModel: MessageModel): Result<MessageModel>
}