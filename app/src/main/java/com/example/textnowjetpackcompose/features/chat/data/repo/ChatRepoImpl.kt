package com.example.textnowjetpackcompose.features.chat.data.repo

import android.util.Log
import com.example.textnowjetpackcompose.features.chat.data.remote.ChatApi
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
            Log.d("SendMessage", "Success: message sent to $receiverId")
            return Result.success(result)
        } catch (e: Exception) {
            Log.e("SendMessage", "Failed to send message to $receiverId", e)
            return Result.failure(e)
        }
    }
}