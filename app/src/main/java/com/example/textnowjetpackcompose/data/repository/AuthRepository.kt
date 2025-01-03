package com.example.textnowjetpackcompose.data.repository

import com.example.textnowjetpackcompose.data.model.SignupRequest
import com.example.textnowjetpackcompose.data.model.AuthUserResponse

interface AuthRepository {
    suspend fun signup(request: SignupRequest): Result<AuthUserResponse>
    suspend fun checkAuth() : AuthUserResponse
}

