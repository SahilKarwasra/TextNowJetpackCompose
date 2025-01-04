package com.example.textnowjetpackcompose.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.textnowjetpackcompose.data.model.UserResponse
import com.example.textnowjetpackcompose.data.repository.MessageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatViewModels(
    private val messageRepository: MessageRepository
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading
    private val _users = MutableStateFlow<List<UserResponse>>(emptyList())
    val Users: StateFlow<List<UserResponse>> get() = _users
    val errorMessage = mutableStateOf<String?>(null)

    suspend fun getUsers() {
        _isLoading.value = true
        errorMessage.value = null
        try {
            val result = messageRepository.getUsers()
            result.fold(
                onSuccess = {
                    _users.value = it
                    Log.d("ChatViewModel", "getUsers: Success ${it.size}")
                },
                onFailure = {
                    errorMessage.value = it.message
                }
            )
        } catch (e: Exception) {
            errorMessage.value = e.message ?: "An Unexpected message occurred"
        }
    }
}
