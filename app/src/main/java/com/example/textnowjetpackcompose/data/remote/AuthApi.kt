package com.example.textnowjetpackcompose.data.remote

import com.example.textnowjetpackcompose.data.model.SignupRequest
import com.example.textnowjetpackcompose.data.model.AuthUserResponse
import com.example.textnowjetpackcompose.data.model.LoginRequest

interface AuthApi {
    suspend fun signup(request: SignupRequest): AuthUserResponse
    suspend fun login(request: LoginRequest): AuthUserResponse
    suspend fun checkAuth(): AuthUserResponse
}