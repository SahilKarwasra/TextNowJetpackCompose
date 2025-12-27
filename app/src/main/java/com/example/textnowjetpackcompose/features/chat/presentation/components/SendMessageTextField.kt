package com.example.textnowjetpackcompose.features.chat.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.textnowjetpackcompose.features.chat.presentation.viewmodel.ChatViewModel
import com.example.textnowjetpackcompose.features.home.domain.model.LastMessage
import com.example.textnowjetpackcompose.features.home.presentation.viewmodel.HomeScreenState
import com.example.textnowjetpackcompose.features.home.presentation.viewmodel.HomeScreenViewModel

@Composable
fun SendMessageTextField(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel,
    receiverId: String,
    userId: String
) {
    Surface(
        modifier = modifier.padding(horizontal = 20.dp)
            .navigationBarsPadding()
    ) {

        val message by chatViewModel.onSendTextFieldValue

        TextField(
            value = message,
            onValueChange = chatViewModel::onSendTextFieldValueChange,
            placeholder = { Text(text = "Type your message...") },
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth().imePadding(),
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (message.isNotBlank()) {
                            chatViewModel.sendMessage(
                                receiverId = receiverId,
                                content = message.trimEnd(),
                                senderId = userId
                            )
                            chatViewModel.onSendTextFieldValueChange("")
                        }
                    },
                    enabled = message.isNotBlank()
                ) {
                    Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = "Send")
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (message.isNotBlank()) {
                        chatViewModel.sendMessage(
                            receiverId = receiverId,
                            content = message.trimEnd(),
                            senderId = userId
                        )
                        chatViewModel.onSendTextFieldValueChange("")
                    }
                }
            ),
        )
    }
}
