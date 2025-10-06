package com.example.textnowjetpackcompose.features.chat.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.textnowjetpackcompose.config.SocketHandler
import com.example.textnowjetpackcompose.features.chat.domain.model.MessageModel
import com.example.textnowjetpackcompose.features.chat.domain.repo.ChatRepo
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class ChatViewModel(
    private val chatRep: ChatRepo,
): ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ChatEvent?>()
    val events = _events.asSharedFlow()

    private var socket: Socket? = null
    private val json = Json { ignoreUnknownKeys = true }

    var onSendTextFieldValue = mutableStateOf("")
        private set

    fun onSendTextFieldValueChange(newValue: String) {
        onSendTextFieldValue.value = newValue
    }

    // initialize Socket
    fun initializeSocket(userId: String) {
        try {
            SocketHandler.setSocket(userId)
            socket = SocketHandler.getSocket().apply {
                off()

                on(Socket.EVENT_CONNECT) {
                    Log.d("SocketIO", "Connected to server")
                    _uiState.value = _uiState.value.copy(isConnected = true)
                }

                on(Socket.EVENT_DISCONNECT) {
                    Log.d("SocketIO", "Disconnected from server")
                    _uiState.value = _uiState.value.copy(isConnected = false)
                }

                on(Socket.EVENT_CONNECT_ERROR) { args ->
                    Log.e("SocketIO", "Connection error: ${args.toList()}")
                    _uiState.value = _uiState.value.copy(isConnected = false)
                }

                on("newMessage") { args ->
                    if (args.isNotEmpty()) {
                        try {
                            val newMessages = json.decodeFromString<MessageModel>(args[0].toString())
                            viewModelScope.launch {
                                _uiState.update { state ->
                                    val messagesExists = state.messages.any {
                                        it.createdAt == newMessages.createdAt
                                    }
                                    if (!messagesExists) {
                                        state.copy(
                                            messages = (state.messages + newMessages).sortedBy { it.createdAt },
                                        )
                                    } else {
                                        state
                                    }
                                }
                                _events.emit(ChatEvent.ScrollToBottom)
                            }
                        } catch (e: Exception) {
                            Log.e("Socket newMessage", "Error parsing message", e)
                        }
                    }
                }
            }
            SocketHandler.establishConnection()
        } catch (e: Exception) {
            Log.e("SocketIO", "Error initializing socket", e)
            viewModelScope.launch {
                _events.emit(ChatEvent.ShowError("Failed to Connect"))
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
                                messages = messages.sortedBy { msg -> msg.createdAt },
                                isLoading = false
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
                        _events.emit(ChatEvent.ShowError(exception.message ?: "Failed to load messages"))
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

        val message = MessageModel(
            senderId = senderId,
            receiverId = receiverId,
            text = content,
            createdAt = System.currentTimeMillis().toString()
        )

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    messages = (it.messages + message).sortedBy { msg -> msg.createdAt },
                    isSendingMessage = true
                )
            }

            try {
                val response = chatRep.sendMessage(receiverId, message)

                response.fold(
                    onSuccess = {
                        Log.d("ChatViewModel sendMessage", "Message sent successfully")
                        _events.emit(ChatEvent.MessageSent)
                        _events.emit(ChatEvent.ScrollToBottom)
                    },
                    onFailure = {
                        Log.e("ChatViewModel sendMessage", "Failed to send message", it)
                        _events.emit(ChatEvent.ShowError("Failed to send message"))
                    }
                )
            } catch (e: Exception) {
                Log.e("ChatViewModel sendMessage", "Error sending message", e)
                _events.emit(ChatEvent.ShowError("Failed to send message"))
            } finally {
                _uiState.update { it.copy(isSendingMessage = false) }
            }
        }

        fun disconnectSocket() {
            try {
                socket?.off()
                SocketHandler.closeConnection()
                socket = null
                _uiState.update { it.copy(isConnected = false) }
                Log.d("Disconnect Socket", "Socket disconnected")
            } catch (e: Exception) {
                Log.e("Disconnect Socket", "Error disconnecting socket", e)
            }
        }

        fun onCleared() {
            super.onCleared()
            disconnectSocket()
        }
    }






}