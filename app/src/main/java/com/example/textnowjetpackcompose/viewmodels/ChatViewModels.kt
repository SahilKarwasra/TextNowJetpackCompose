package com.example.textnowjetpackcompose.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.textnowjetpackcompose.data.SocketHandler
import com.example.textnowjetpackcompose.data.model.MessageModel
import com.example.textnowjetpackcompose.data.model.UserResponse
import com.example.textnowjetpackcompose.data.repository.MessageRepository
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

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

    private lateinit var socket: Socket

    fun initializeSocket(userId: String) {

        SocketHandler.setSocket(userId)
        socket = SocketHandler.getSocket()
        SocketHandler.establishConnection()

        // Listen for new messages
        socket.on("newMessage") { args ->
            if (args.isNotEmpty()) {
                Log.d("WebSocket", "New message event received: $args")
                val data = args[0] as JSONObject
                val newMessage = MessageModel(
                    senderId = data.getString("senderId"),
                    receiverId = data.getString("receiverId"),
                    text = data.optString("text", ""),
                    image = data.optString("image"),
                    createdAt = data.getString("createdAt"),
                    updatedAt = data.optString("updatedAt", "")
                )
                viewModelScope.launch {
                    withContext(Dispatchers.Main) {
                        Log.d("WebSocket", "Adding message: $newMessage")
                        _messageText.value = _messageText.value + newMessage
                    }
                }
            }
        }
    }
    fun disconnectSocket() {
        SocketHandler.closeConnection()
    }


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

    suspend fun sendMessage(receiverId: String, messageModel: MessageModel) {
        try {
            val response = messageRepository.sendMessage(receiverId, messageModel)
            if (response.status.value == 200 || response.status.value == 201) {
                Log.d("ChatViewModel", "sendMessage: Message sent successfully")
            } else {
                Log.e("ChatViewModel", "sendMessage: Failed to send message  ${response.status.value}")
            }
        } catch (e: Exception) {
            Log.e("ChatViewModel", "sendMessage: Error sending message", e)
        }
    }
}
