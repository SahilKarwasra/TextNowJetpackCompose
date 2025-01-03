package com.example.textnowjetpackcompose.data.repository

import com.example.textnowjetpackcompose.data.model.SignupRequest
import com.example.textnowjetpackcompose.data.model.AuthUserResponse
import com.example.textnowjetpackcompose.data.model.LoginRequest

interface AuthRepository {
    suspend fun signup(request: SignupRequest): Result<AuthUserResponse>
    suspend fun login(request: LoginRequest): Result<AuthUserResponse>
    suspend fun checkAuth() : Result<AuthUserResponse>
}

