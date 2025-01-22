package com.example.textnowjetpackcompose.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.textnowjetpackcompose.data.repository.AuthRepository
import com.example.textnowjetpackcompose.data.repository.MessageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GeminiViewModel (
    private val messageRepository: MessageRepository,
): ViewModel() {

    private val _generatedText = MutableStateFlow<Result<String>?>(null)
    val generatedText: StateFlow<Result<String>?> = _generatedText

    // Function to call generateText in the repository
    fun generateText(prompt: String) {
        viewModelScope.launch {
            _generatedText.value = messageRepository.generateText(prompt)
        }
    }

}