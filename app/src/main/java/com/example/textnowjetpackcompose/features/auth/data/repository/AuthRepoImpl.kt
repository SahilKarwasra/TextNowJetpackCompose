package com.example.textnowjetpackcompose.features.auth.data.repository

import com.example.textnowjetpackcompose.features.auth.data.remote.AuthApi
import com.example.textnowjetpackcompose.features.auth.domain.model.LoginRequest
import com.example.textnowjetpackcompose.features.auth.domain.model.SignupRequest
import com.example.textnowjetpackcompose.features.home.domain.model.UserResponse
import com.example.textnowjetpackcompose.features.auth.domain.repository.AuthRepo
import io.ktor.client.statement.HttpResponse

class AuthRepoImpl(
    private val authApi: AuthApi
): AuthRepo {
    override suspend fun signup(request: SignupRequest): Result<UserResponse> {
        return try {
            val response = authApi.signUp(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(request: LoginRequest): Result<UserResponse> {
        return try {
            val response = authApi.login(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkAuth(): Result<UserResponse> {
        return try {
            val response = authApi.checkAuth()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(imageUri: ByteArray): HttpResponse {
        return try {
            authApi.updateProfile(imageUri)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun logout(): HttpResponse {
        return try {
            authApi.logout()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun saveFcmToken(fcmToken: String): Result<Unit> {
        return try {
            authApi.saveFcmToken(fcmToken)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}