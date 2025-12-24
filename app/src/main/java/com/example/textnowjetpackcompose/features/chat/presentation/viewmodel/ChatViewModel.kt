package com.example.textnowjetpackcompose.features.chat.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.textnowjetpackcompose.config.SocketHandler
import com.example.textnowjetpackcompose.features.chat.domain.model.MessageEntity
import com.example.textnowjetpackcompose.features.chat.domain.model.MessageModel
import com.example.textnowjetpackcompose.features.chat.domain.model.MessageRequest
import com.example.textnowjetpackcompose.features.chat.domain.repo.ChatRepo
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class ChatViewModel(
    private val chatRep: ChatRepo,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()


    private val json = Json { ignoreUnknownKeys = true }

    var onSendTextFieldValue = mutableStateOf("")
        private set

    fun onSendTextFieldValueChange(newValue: String) {
        onSendTextFieldValue.value = newValue
    }

    init {
        initializeSocketEvents()
    }

    fun initializeSocketEvents() {
        val socket = SocketHandler.getSocket()

        socket.off(Socket.EVENT_CONNECT)
        socket.off(Socket.EVENT_DISCONNECT)
        socket.off("newMessage")

        _uiState.update { it.copy(isConnected = socket.connected()) }

        socket.on(Socket.EVENT_CONNECT) {
            Log.d("SocketIO", "Connected")
            _uiState.update { it.copy(isConnected = true) }
        }

        socket.on(Socket.EVENT_DISCONNECT) {
            Log.d("SocketIO", "Disconnected")
            _uiState.update { it.copy(isConnected = false) }
        }

        socket.on("newMessage") { args ->
            if (args.isNotEmpty()) {
                try {
                    val payload = args[0].toString()
                    val newMessage = json.decodeFromString<MessageModel>(payload)

                    viewModelScope.launch {
                        _uiState.update { state ->
                            val exists = newMessage._id?.let { id -> state.messages.any { it._id == id } }
                                ?: state.messages.any { it.createdAt == newMessage.createdAt }

                            if (exists) state
                            else state.copy(messages = (state.messages + newMessage).sortedBy { it.createdAt })
                        }
                    }
                } catch (e: Exception) {
                    Log.e("ChatViewModel", "Failed to parse incoming message", e)
                }
            }
        }
    }


    fun loadMessages(receiverId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val result = chatRep.getMessages(receiverId)
                result.fold(
                    onSuccess = { messages ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                messages = messages.sortedBy { it.createdAt }
                            )
                        }
                    },
                    onFailure = { exception ->
                        Log.e("ChatViewModel loadMessages", "Failed to load messages", exception)
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = exception.message
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                Log.e("ChatViewModel loadMessages", "Error loading messages", e)
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun sendMessage(receiverId: String, content: String, senderId: String) {
        if (content.isBlank()) return

        val message = MessageRequest(
            senderId = senderId,
            receiverId = receiverId,
            text = content,
            createdAt = System.currentTimeMillis().toString(),
        )

        viewModelScope.launch {

            try {
                val response = chatRep.sendMessage(receiverId, message)

                response.fold(
                    onSuccess = { msg ->
                        Log.d("ChatViewModel sendMessage", "Message sent successfully")
                        _uiState.update {
                            it.copy(
                                messages = it.messages + msg,
                                isSendingMessage = true
                            )
                        }
                    },
                    onFailure = {
                        Log.e("ChatViewModel sendMessage", "Failed to send message", it)
                    }
                )
                SocketHandler.getSocket().emit("sendMessage", json.encodeToString(MessageRequest.serializer(), message))
            } catch (e: Exception) {
                Log.e("ChatViewModel sendMessage", "Error sending message", e)
            } finally {
                _uiState.update { it.copy(isSendingMessage = false) }
            }
        }
    }
}









