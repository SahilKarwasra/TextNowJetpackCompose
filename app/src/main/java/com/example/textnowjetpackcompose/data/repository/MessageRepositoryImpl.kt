package com.example.textnowjetpackcompose.data.repository

import android.util.Log
import com.example.textnowjetpackcompose.data.model.UserResponse
import com.example.textnowjetpackcompose.data.remote.MessageApi

class MessageRepositoryImpl(
    private val messageApi: MessageApi
): MessageRepository {
    override suspend fun getUsers(): Result<List<UserResponse>> {
        Log.d("MessageRepositoryImpl", "getUsers: Starting GetUsers Request from MessageRepositoryImpl")
        return try {
            val response = messageApi.getUsers()
            Log.d("MessageRepositoryImpl", "getUsers: Response from MessageRepositoryImpl: $response")
            Result.success(response)
        } catch (e: Exception) {
            Log.e("MessageRepositoryImpl", "getUsers: Error during GetUsers Request from MessageRepositoryImpl", e)
            Result.failure(e)
        }
    }
}