package com.example.textnowjetpackcompose.features.auth.domain.repository

import com.example.textnowjetpackcompose.features.auth.domain.model.LoginRequest
import com.example.textnowjetpackcompose.features.auth.domain.model.SignupRequest
import com.example.textnowjetpackcompose.features.home.domain.model.UserResponse
import io.ktor.client.statement.HttpResponse

interface AuthRepo {
    suspend fun signup(request: SignupRequest): Result<UserResponse>
    suspend fun login(request: LoginRequest): Result<UserResponse>
    suspend fun checkAuth() : Result<UserResponse>
    suspend fun updateProfile(imageUri: ByteArray): HttpResponse
    suspend fun logout() : HttpResponse
}