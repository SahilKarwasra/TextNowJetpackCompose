package com.example.textnowjetpackcompose.features.auth.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.textnowjetpackcompose.features.auth.domain.model.LoginRequest
import com.example.textnowjetpackcompose.features.auth.domain.model.SignupRequest
import com.example.textnowjetpackcompose.features.home.domain.model.UserResponse
import com.example.textnowjetpackcompose.features.auth.domain.repository.AuthRepo
import com.example.textnowjetpackcompose.config.SocketHandler
import com.example.textnowjetpackcompose.config.PreferenceManager
import io.ktor.client.call.body
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepo, applicationContext: Context
) : ViewModel() {
    private val preferenceManager = PreferenceManager(context = applicationContext)
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    var formState by mutableStateOf(AuthFormState())
        private set

    fun onEmailChange(newEmail: String) {
        formState = formState.copy(email = newEmail)
    }

    fun onPasswordChange(newPassword: String) {
        formState = formState.copy(password = newPassword)
    }

    fun onUsernameChange(newUsername: String) {
        formState = formState.copy(username = newUsername)
    }

    init {
        checkAuth()
    }

    private val _isProfilePicUpdating = MutableStateFlow(false)
    val isProfilePicUpdating = _isProfilePicUpdating.asStateFlow()

    private val _profilePicUrl = MutableStateFlow(preferenceManager.getUser()?.profilePic)
    val profilePicUrl = _profilePicUrl.asStateFlow()
    var selectedImageUri =  mutableStateOf<ByteArray?>(null)


    fun signUp(request: SignupRequest) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = authRepository.signup(request)
                result.fold(onSuccess = {
                    _authState.value = AuthState.Authenticated(it)
                    preferenceManager.saveUser(it)
                    connectSocket(it.id)
                }, onFailure = {
                    _authState.value = AuthState.Error(it.message ?: "An unexpected error occurred")
                    delay(500)
                    _authState.value = AuthState.UnAuthenticated
                })
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "An unexpected error occurred")
                delay(500)
                _authState.value = AuthState.UnAuthenticated

            }
        }
    }

    fun login(request: LoginRequest) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = authRepository.login(request)
                result.fold(onSuccess = {
                    _authState.value = AuthState.Authenticated(it)
                    preferenceManager.saveUser(it)
                    connectSocket(it.id)
                }, onFailure = {
                    _authState.value = AuthState.Error(it.message ?: "An unexpected error occurred")
                    delay(500)
                    _authState.value = AuthState.UnAuthenticated
                })
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "An unexpected error occurred")
                delay(500)
                _authState.value = AuthState.UnAuthenticated
            }

        }
    }

    private fun checkAuth() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                viewModelScope.launch {
                    _authState.value = AuthState.Loading
                    try {
                        val result = authRepository.checkAuth()
                        result.fold(
                            onSuccess = {
                                _authState.value = AuthState.Authenticated(it)
                                preferenceManager.saveUser(it)
                                connectSocket(it.id)
                            },
                            onFailure = {
                                _authState.value = AuthState.UnAuthenticated
                            }
                        )
                    } catch (e: Exception) {
                        _authState.value = AuthState.UnAuthenticated
                    }
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "An unexpected error occurred")
                delay(5000)
                _authState.value = AuthState.UnAuthenticated
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                authRepository.logout()
                disconnectSocket()
                _authState.value = AuthState.UnAuthenticated
                preferenceManager.deleteUser()
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    fun updateProfilePic(imageBytes: ByteArray?, onResult: (String) -> Unit) {
        _isProfilePicUpdating.value = true

        if (imageBytes == null) {
            Log.e("ProfileViewModel", "updateProfilePic: Image bytes are null")
            onResult("Image bytes are null")
            _isProfilePicUpdating.value = false
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = authRepository.updateProfile(imageBytes)
                val updatedUser = response.body<UserResponse>()
                _authState.value = AuthState.Authenticated(updatedUser)
                _profilePicUrl.value = updatedUser.profilePic
                preferenceManager.saveUser(updatedUser)
                onResult("Profile Picture Updated Successfully")
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "An unexpected error occurred during profile update")
                onResult("Failed to update profile picture")
            } finally {
                _isProfilePicUpdating.value = false
                selectedImageUri.value = null
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