package com.example.textnowjetpackcompose.data.repository

import com.example.textnowjetpackcompose.data.model.SignupRequest
import com.example.textnowjetpackcompose.data.model.UserResponse
import com.example.textnowjetpackcompose.data.model.LoginRequest
import io.ktor.client.statement.HttpResponse

interface AuthRepository {
    suspend fun signup(request: SignupRequest): Result<UserResponse>
    suspend fun login(request: LoginRequest): Result<UserResponse>
    suspend fun checkAuth() : Result<UserResponse>
    suspend fun updateProfile(imageUri: ByteArray): HttpResponse
    suspend fun logout() : HttpResponse
}

