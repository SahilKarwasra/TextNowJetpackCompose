package com.example.textnowjetpackcompose.features.chat.data.repo

import com.example.textnowjetpackcompose.features.chat.data.remote.ChatApi
import com.example.textnowjetpackcompose.features.chat.domain.model.MessageEntity
import com.example.textnowjetpackcompose.features.chat.domain.model.MessageModel
import com.example.textnowjetpackcompose.features.chat.domain.model.MessageRequest
import com.example.textnowjetpackcompose.features.chat.domain.repo.ChatRepo

class ChatRepoImpl(
    private val chatApi: ChatApi,
): ChatRepo {

    override suspend fun getMessages(
        receiverId: String
    ): Result<List<MessageModel>> {
        try {
            val result = chatApi.getMessages(receiverId)
            val entities = result.map { message ->
                MessageEntity(
                    senderId = message.senderId,
                    receiverId = message.receiverId,
                    text = message.text,
                    image = message.image,
                    createdAt = message.createdAt,
                    updatedAt = message.updatedAt,
                    id = message._id
                )
            }
            return Result.success(result)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }


    override suspend fun sendMessage(
        receiverId: String,
        messageRequest: MessageRequest
    ): Result<MessageModel> {
        try {
            val result = chatApi.sendMessage(receiverId, messageRequest)
            return Result.success(result)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}