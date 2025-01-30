package com.example.textnowjetpackcompose.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.textnowjetpackcompose.data.model.UserResponse
import com.example.textnowjetpackcompose.data.model.LoginRequest
import com.example.textnowjetpackcompose.data.model.SignupRequest
import com.example.textnowjetpackcompose.data.remote.AuthApi
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val dataStore: DataStore<Preferences>
) : AuthRepository {
    override suspend fun signup(request: SignupRequest): Result<UserResponse> {
        return try {
            val response = authApi.signup(request)
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

        Log.d("AuthRepositoryImpl", "checkAuth: Starting checkAuth request AuthRepositoryImpl")
        return try {
            val response = authApi.checkAuth()
            Result.success(response)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "checkAuth: Error during checkAuth AuthRepositoryImpl", e)
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(bytes: ByteArray): HttpResponse {
        return withContext(Dispatchers.IO) {
            try {
                authApi.updateProfile(bytes)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun logout(): HttpResponse {
        return withContext(Dispatchers.IO) {
            try {
                authApi.logout()
            } catch (e: Exception) {
                throw e
            }
        }
    }
}