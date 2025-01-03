package com.example.textnowjetpackcompose.data.remote

import com.example.textnowjetpackcompose.data.model.SignupRequest
import com.example.textnowjetpackcompose.data.model.AuthUserResponse

interface AuthApi {
    suspend fun signup(request: SignupRequest): AuthUserResponse
    suspend fun checkAuth(): AuthUserResponse
}