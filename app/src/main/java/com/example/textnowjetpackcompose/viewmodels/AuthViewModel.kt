package com.example.textnowjetpackcompose.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.textnowjetpackcompose.data.SocketHandler
import com.example.textnowjetpackcompose.data.model.SignupRequest
import com.example.textnowjetpackcompose.data.model.UserResponse
import com.example.textnowjetpackcompose.data.model.LoginRequest
import com.example.textnowjetpackcompose.data.repository.AuthRepository
import com.example.textnowjetpackcompose.screens.utils.PreferenceManager
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    applicationContext: Context
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading
    val errorMessage = mutableStateOf<String?>(null)
    private val _userResponse = MutableStateFlow<UserResponse?>(null)
    val userResponse: StateFlow<UserResponse?> get() = _userResponse
    var isAuthenticated by mutableStateOf(false)

    val preferenceManager = PreferenceManager(context = applicationContext)


    fun signup(signupRequest: SignupRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = authRepository.signup(signupRequest)
                result.fold(
                    onSuccess = {
                        _userResponse.value = it
                        preferenceManager.saveUser(it)
                        connectSocket(it.id)
                        isAuthenticated = true
                    },
                    onFailure = { error ->
                        Log.d("Signup Error", error.message ?: "Unknown error")
                    }
                )
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
                        _userResponse.value = it
                        connectSocket(it.id)
                    },
                    onFailure = {
                        errorMessage.value = it.message
                        Log.d("Login Failure", "login: Error ${it.message}")
                    }
                )
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "An Unexpected message occurred during login"
                Log.d("Login Catch", "login: Error ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun checkAuth() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = preferenceManager.getUser()
                if (user != null) {
                    _userResponse.value = user
                    connectSocket(user.id)
                    isAuthenticated = true
                } else {
                    val result = authRepository.checkAuth()
                    result.fold(
                        onSuccess = {
                            _userResponse.value = it
                            preferenceManager.saveUser(it)
                            connectSocket(it.id)
                            isAuthenticated = true
                        },
                        onFailure = { error ->
                            Log.d("CheckAuth Error", error.message ?: "Unknown error")
                        }
                    )
                }
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
                    _userResponse.value = null
                    preferenceManager.deleteUser()
                    isAuthenticated = false
                    disconnectSocket()
                    Log.d("LogoutViewModel", "logout: User is logged out")
                } else {
                    val errorBody = response.body<String>()
                    errorMessage.value = "Logout failed: ${response.status} - $errorBody"
                    Log.e(
                        "LogoutViewModel",
                        "logout: HTTP error: ${response.status}, body: $errorBody"
                    )
                }
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "An Unexpected message occurred during logout"
                Log.e("LogoutViewModel", "logout: Error during logout", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateProfilePic(profilePic: String) {
        viewModelScope.launch {
            _isLoading.value = true
            errorMessage.value = null
            try {
                val response = authRepository.updateProfile(profilePic)
                if (response.status.isSuccess()) {
                    val updatedUser = response.body<UserResponse>()
                    _userResponse.value = updatedUser
                    preferenceManager.saveUser(updatedUser)
                    Log.d("ProfileViewModel", "updateProfilePic: Profile picture updated")
                } else {
                    val errorBody = response.body<String>()
                    errorMessage.value = "Profile picture"
                }
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "An Unexpected message occurred during profile update"
                Log.e("ProfileViewModel", "updateProfilePic: Error during profile update", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun connectSocket(userId: String) {
        SocketHandler.setSocket(userId)
        SocketHandler.establishConnection()
        Log.d("WebSocket", "Connected with userId: $userId")
    }

    private fun disconnectSocket() {
        SocketHandler.closeConnection()
        Log.d("WebSocket", "Disconnected")
    }

}