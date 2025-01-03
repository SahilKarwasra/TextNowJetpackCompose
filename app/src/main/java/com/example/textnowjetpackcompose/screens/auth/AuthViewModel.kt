package com.example.textnowjetpackcompose.screens.auth

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.textnowjetpackcompose.data.model.SignupRequest
import com.example.textnowjetpackcompose.data.model.AuthUserResponse
import com.example.textnowjetpackcompose.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel (private val authRepository: AuthRepository) : ViewModel()  {

    private val isLoading = mutableStateOf(false)
    private val errorMessage = mutableStateOf<String?>(null)
    private val authUserResponse = mutableStateOf<AuthUserResponse?>(null)


    fun signup(signupRequest: SignupRequest) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            try {
                val result = authRepository.signup(signupRequest)
                result.fold(
                    onSuccess = {
                        authUserResponse.value = it
                    },
                    onFailure = {
                        errorMessage.value = it.message
                        Log.d("Signup Failure", "signup: Error ${it.message}")
                    }
                )
            } catch (
                e: Exception
            ) {
                errorMessage.value = e.message ?:"An Unexpected message occurred during signup"
                Log.d("Signup Catch", "signup: Error ${e.message}")
            } finally {
                isLoading.value = false
            }

        }
    }

    suspend fun checkAuth(): Result<AuthUserResponse> {
        return try {
            val authUserResponse = authRepository.checkAuth()
            Result.success(authUserResponse)
        } catch (e: Exception) {
            Log.e("AuthViewModel", "checkAuth: Error during checkAuth", e)
            Result.failure(e)
        }
    }
}