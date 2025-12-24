package com.example.textnowjetpackcompose.features.chat.domain.repo

import com.example.textnowjetpackcompose.features.chat.domain.model.MessageEntity
import com.example.textnowjetpackcompose.features.chat.domain.model.MessageModel
import com.example.textnowjetpackcompose.features.chat.domain.model.MessageRequest
import kotlinx.coroutines.flow.Flow

interface ChatRepo {
    suspend fun getMessages(receiverId: String): Result<List<MessageModel>>
    suspend fun sendMessage(receiverId: String, messageRequest: MessageRequest): Result<MessageModel>
}