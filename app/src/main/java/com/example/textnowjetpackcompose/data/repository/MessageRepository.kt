package com.example.textnowjetpackcompose.data.repository

import com.example.textnowjetpackcompose.data.model.MessageModel
import com.example.textnowjetpackcompose.data.model.UserResponse

interface MessageRepository {
    suspend fun getUsers(): Result<List<UserResponse>>
    suspend fun getMessages(receiverId: String): Result<List<MessageModel>>
}