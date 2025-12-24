package com.example.textnowjetpackcompose.features.chat.presentation.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.textnowjetpackcompose.config.SocketHandler
import com.example.textnowjetpackcompose.features.chat.presentation.components.ChatBubble
import com.example.textnowjetpackcompose.features.chat.presentation.components.SendMessageTextField
import com.example.textnowjetpackcompose.features.chat.presentation.viewmodel.ChatViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    userId: String,
    receiverId: String,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.loadMessages(receiverId)
    }

    val scrollState = rememberLazyListState()

    LaunchedEffect(state.messages) {
        scrollState.animateScrollToItem(state.messages.size)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .weight(1f),
            state = scrollState,
            verticalArrangement = Arrangement.Top
        ) {
            items(state.messages) { msg ->
                msg.text?.let {
                    Log.d("UsersIds", "ChatScreen: ${msg.senderId} $userId")
                    ChatBubble(
                        text = it,
                        isMine = msg.senderId == userId,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }
            }
        }
        SendMessageTextField(
            modifier = Modifier,
            chatViewModel = viewModel,
            receiverId = receiverId,
            userId = userId,
        )
    }
}

