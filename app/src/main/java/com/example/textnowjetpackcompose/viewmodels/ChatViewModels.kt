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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class ChatViewModels(
    private val messageRepository: MessageRepository
) : ViewModel() {


    private val _messageText = MutableStateFlow<List<MessageModel>>(emptyList())
    private val messageText: StateFlow<List<MessageModel>> = _messageText.asStateFlow()

    private val _chatState = MutableStateFlow(ChatState())
    val chatState = _chatState.combine(messageText) { state, messages ->
        state.copy(
            messages = messages
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ChatState())

    private var socket: Socket? = null

    fun initializeSocket(userId: String) {

        SocketHandler.setSocket(userId)
        socket = SocketHandler.getSocket()
        SocketHandler.establishConnection()

        // Listen for new messages
        socket?.on("newMessage") { args ->
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

                Log.d("WebSocket", "Adding message: $newMessage")
                _messageText.value += newMessage

            }
        }
    }

    fun disconnectSocket() {
        socket?.off("newMessage")
        SocketHandler.closeConnection()
    }


    suspend fun getUsers() {
        _chatState.value = _chatState.value.copy(
            errorMessage = null,
            isLoading = true
        )
        try {
            val result = messageRepository.getUsers()
            result.fold(
                onSuccess = {
                    _chatState.value = _chatState.value.copy(
                        users = it,
                        isLoading = false
                    )

                },
                onFailure = {
                    _chatState.value = _chatState.value.copy(
                        errorMessage = it.message
                    )
                }
            )
        } catch (e: Exception) {
            _chatState.value = _chatState.value.copy(
                errorMessage = e.message
            )
        }
    }

    suspend fun getMessages(receiverId: String) {
        _chatState.value = _chatState.value.copy(
            errorMessage = null,
            isLoading = true
        )
        try {

            val result = messageRepository.getMessages(receiverId)
            result.fold(
                onSuccess = {
                    _messageText.value = it
                    _chatState.value = _chatState.value.copy(
                        isLoading = false
                    )
                    Log.d("ChatViewModel", "getMessages: Success ${it}")
                },
                onFailure = {
                    _chatState.value = _chatState.value.copy(
                        errorMessage = it.message
                    )
                }
            )
        } catch (e: Exception) {
            _chatState.value = _chatState.value.copy(
                errorMessage = e.message
            )
        }
    }

    suspend fun sendMessage(receiverId: String, messageModel: MessageModel) {
        try {
            _messageText.value = _messageText.value.plus(messageModel)
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

    override fun onCleared() {
        super.onCleared()
        disconnectSocket()
    }
}
