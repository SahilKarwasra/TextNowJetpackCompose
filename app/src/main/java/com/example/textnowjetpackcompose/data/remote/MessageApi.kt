package com.example.textnowjetpackcompose.data.remote

import com.example.textnowjetpackcompose.data.model.MessageModel
import com.example.textnowjetpackcompose.data.model.UserResponse
import io.ktor.client.statement.HttpResponse

interface MessageApi {
    suspend fun getUsers(): List<UserResponse>
    suspend fun getMessages(receiverId: String): List<MessageModel>
    suspend fun sendMessage(receiverId: String, messageModel: MessageModel): HttpResponse
    suspend fun generateText(prompt: String): String
}