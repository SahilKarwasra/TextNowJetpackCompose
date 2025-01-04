package com.example.textnowjetpackcompose.data.remote

import com.example.textnowjetpackcompose.data.model.UserResponse

interface MessageApi {
    suspend fun getUsers(): List<UserResponse>
}