package com.example.textnowjetpackcompose.screens.aibot

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.textnowjetpackcompose.viewmodels.GeminiViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AiBotScreen(
    messageViewModel: GeminiViewModel = koinViewModel()
) {
    val generateTextState = messageViewModel.generatedText.collectAsState()
    var prompt by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = prompt,
            onValueChange = { prompt = it },
            label = { Text("Enter prompt") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                messageViewModel.generateText(prompt)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generate Text")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            generateTextState.value == null -> {
                Text("Enter a prompt to generate text.")
            }
            generateTextState.value?.isSuccess == true -> {
                val responseText = generateTextState.value?.getOrNull()
                Text("Generated Text: $responseText")
            }
            generateTextState.value?.isFailure == true -> {
                val error = generateTextState.value?.exceptionOrNull()
                Text("Error: ${error?.message}")
            }
        }
    }
}

