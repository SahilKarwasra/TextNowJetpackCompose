package com.example.textnowjetpackcompose.data.remote

import com.example.textnowjetpackcompose.data.model.SignupRequest
import com.example.textnowjetpackcompose.data.model.UserResponse
import com.example.textnowjetpackcompose.data.model.LoginRequest
import io.ktor.client.statement.HttpResponse

interface AuthApi {
    suspend fun signup(request: SignupRequest): UserResponse
    suspend fun login(request: LoginRequest): UserResponse
    suspend fun checkAuth(): UserResponse
    suspend fun logout(): HttpResponse
}