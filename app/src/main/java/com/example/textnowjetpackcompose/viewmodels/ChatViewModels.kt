package com.example.textnowjetpackcompose.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.textnowjetpackcompose.data.model.MessageModel
import com.example.textnowjetpackcompose.data.model.UserResponse
import com.example.textnowjetpackcompose.data.repository.MessageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatViewModels(
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _users = MutableStateFlow<List<UserResponse>>(emptyList())
    val Users: StateFlow<List<UserResponse>> get() = _users

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()


    private val _messageText = MutableStateFlow<List<MessageModel>>(emptyList())
    val messageText: StateFlow<List<MessageModel>> = _messageText.asStateFlow()

    suspend fun getUsers() {
        _isLoading.value = true
        _errorMessage.value = null
        try {
            val result = messageRepository.getUsers()
            result.fold(
                onSuccess = {
                    _users.value = it
                    Log.d("ChatViewModel", "getUsers: Success ${Users.value}")
                    _isLoading.value = false
                },
                onFailure = {
                    _errorMessage.value = it.message
                }
            )
        } catch (e: Exception) {
            _errorMessage.value = e.message ?: "An Unexpected message occurred"
        }
    }

    suspend fun getMessages(receiverId: String) {
        _isLoading.value = true
        _errorMessage.value = null
        try {
            val result = messageRepository.getMessages(receiverId)
            result.fold(
                onSuccess = {
                    _messageText.value = it
                    _isLoading.value = false
                    Log.d("ChatViewModel", "getMessages: Success ${messageText.value}")
                },
                onFailure = {
                    _errorMessage.value = it.message
                }
            )
        } catch (e: Exception) {
            _errorMessage.value = e.message ?: "An Unexpected message occurred"
        }
    }
}
