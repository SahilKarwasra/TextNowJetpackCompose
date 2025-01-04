package com.example.textnowjetpackcompose.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.textnowjetpackcompose.data.model.SignupRequest
import com.example.textnowjetpackcompose.data.model.UserResponse
import com.example.textnowjetpackcompose.data.model.LoginRequest
import com.example.textnowjetpackcompose.data.repository.AuthRepository
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel (private val authRepository: AuthRepository) : ViewModel()  {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading
    val errorMessage = mutableStateOf<String?>(null)
    val userResponse = mutableStateOf<UserResponse?>(null)
    var isAuthenticated by mutableStateOf(false)

    fun signup(signupRequest: SignupRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            errorMessage.value = null
            try {
                val result = authRepository.signup(signupRequest)
                result.fold(
                    onSuccess = {
                        userResponse.value = it
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
                _isLoading.value = false
            }

        }
    }

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            errorMessage.value = null
            try {
                val result = authRepository.login(loginRequest)
                result.fold(
                    onSuccess = {
                        userResponse.value = it
                    },
                    onFailure = {
                        errorMessage.value = it.message
                        Log.d("Login Failure", "login: Error ${it.message}")
                    }
                )
            } catch (e: Exception) {
                errorMessage.value = e.message ?:"An Unexpected message occurred during login"
                Log.d("Login Catch", "login: Error ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun checkAuth() {
        errorMessage.value = null
        viewModelScope.launch {
            _isLoading.value = true
            errorMessage.value = null
            try {
                val result = authRepository.checkAuth()
                result.fold(
                    onSuccess = {
                        userResponse.value = it
                        Log.d("CheckAuthViewModel", "checkAuth: User is authenticated")
                        isAuthenticated = true
                    },
                    onFailure = {
                        errorMessage.value = it.message
                        Log.d("CheckAuth Failure", "checkAuth: Error ${it.message}")
                    }
                )
            } catch (e: Exception) {
                errorMessage.value = e.message ?:"An Unexpected message occurred during checkAuth"
                Log.d("CheckAuth Catch", "checkAuth: Error ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _isLoading.value = true
            errorMessage.value = null
            try {
                val response = authRepository.logout()
                if (response.status.isSuccess()) {
                    isAuthenticated = false
                    Log.d("LogoutViewModel", "logout: User is logged out")
                } else {
                    val errorBody = response.body<String>()
                    errorMessage.value = "Logout failed: ${response.status} - $errorBody"
                    Log.e("LogoutViewModel", "logout: HTTP error: ${response.status}, body: $errorBody")
                }
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "An Unexpected message occurred during logout"
                Log.e("LogoutViewModel", "logout: Error during logout", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}