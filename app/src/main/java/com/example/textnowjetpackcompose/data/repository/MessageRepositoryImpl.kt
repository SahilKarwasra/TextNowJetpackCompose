package com.example.textnowjetpackcompose.data.repository

import android.util.Log
import com.example.textnowjetpackcompose.data.model.MessageModel
import com.example.textnowjetpackcompose.data.model.UserResponse
import com.example.textnowjetpackcompose.data.remote.MessageApi
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MessageRepositoryImpl(
    private val messageApi: MessageApi
) : MessageRepository {

    override suspend fun getUsers(): Result<List<UserResponse>> {
        Log.d(
            "MessageRepositoryImpl",
            "getUsers: Starting GetUsers Request from MessageRepositoryImpl"
        )
        return try {
            val response = messageApi.getUsers()
            Log.d(
                "MessageRepositoryImpl",
                "getUsers: Response from MessageRepositoryImpl: $response"
            )
            Result.success(response)
        } catch (e: Exception) {
            Log.e(
                "MessageRepositoryImpl",
                "getUsers: Error during GetUsers Request from MessageRepositoryImpl",
                e
            )
            Result.failure(e)
        }
    }

    override suspend fun getMessages(receiverId: String): Result<List<MessageModel>> {
        Log.d(
            "MessageRepositoryImpl",
            "getMessages: Starting GetMessages Request from MessageRepositoryImpl"
        )
        return try {
            val response = messageApi.getMessages(receiverId)
            Log.d(
                "MessageRepositoryImpl",
                "getMessages: Response from MessageRepositoryImpl: $response"
            )
            Result.success(response)
        } catch (e: Exception) {
            Log.e(
                "MessageRepositoryImpl",
                "getMessages: Error during GetMessages Request from MessageRepositoryImpl",
                e
            )
            Result.failure(e)
        }
    }

    override suspend fun sendMessage(receiverId: String, messageModel: MessageModel): HttpResponse {
        Log.d(
            "MessageRepositoryImpl",
            "sendMessage: Starting SendMessage Request from MessageRepositoryImpl"
        )
        return withContext(Dispatchers.IO) {
            try {
                messageApi.sendMessage(receiverId, messageModel)
            } catch (e: Exception) {
                Log.e(
                    "MessageRepositoryImpl",
                    "sendMessage: Error during SendMessage Request from MessageRepositoryImpl",
                    e
                )
                throw e
            }
        }
    }

    override suspend fun generateText(prompt: String): Result<String> {
        Log.d(
            "MessageRepositoryImpl",
            "generateText: Starting GenerateText Request from MessageRepositoryImpl"
        )
        return try {
            val response = messageApi.generateText(prompt)
            Log.d(
                "MessageRepositoryImpl",
                "generateText: Response from MessageRepositoryImpl: $response",
            )
            Result.success(response)
        } catch (e: Exception) {
            Log.e(
                "MessageRepositoryImpl",
                "generateText: Error during GenerateText Request from MessageRepositoryImpl",
                e
            )
            Result.failure(e)
        }
    }
}