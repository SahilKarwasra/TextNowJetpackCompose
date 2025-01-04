package com.example.textnowjetpackcompose.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.textnowjetpackcompose.data.model.AuthUserResponse
import com.example.textnowjetpackcompose.data.model.LoginRequest
import com.example.textnowjetpackcompose.data.model.SignupRequest
import com.example.textnowjetpackcompose.data.remote.AuthApi

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val dataStore: DataStore<Preferences>
) : AuthRepository {
    override suspend fun signup(request: SignupRequest): Result<AuthUserResponse> {
        return try {
            val response = authApi.signup(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(request: LoginRequest): Result<AuthUserResponse> {
        return try {
            val response = authApi.login(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkAuth(): Result<AuthUserResponse> {

        Log.d("AuthRepositoryImpl", "checkAuth: Starting checkAuth request AuthRepositoryImpl")
        return try {
            val response = authApi.checkAuth()
            Result.success(response)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "checkAuth: Error during checkAuth AuthRepositoryImpl", e)
            Result.failure(e)
        }
    }
}