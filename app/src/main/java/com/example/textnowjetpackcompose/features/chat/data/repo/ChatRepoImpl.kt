package com.example.textnowjetpackcompose.features.chat.data.repo

import com.example.textnowjetpackcompose.features.chat.data.remote.ChatApi
import com.example.textnowjetpackcompose.features.chat.domain.model.MessageModel
import com.example.textnowjetpackcompose.features.chat.domain.repo.ChatRepo

class ChatRepoImpl(
    private val chatApi: ChatApi
): ChatRepo {

    override suspend fun getMessages(
        receiverId: String
    ): Result<List<MessageModel>> {
        try {
            val result = chatApi.getMessages(receiverId)
            return Result.success(result)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun sendMessage(
        receiverId: String,
        messageModel: MessageModel
    ): Result<MessageModel> {
        try {
            val result = chatApi.sendMessage(receiverId, messageModel)
            return Result.success(result)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}