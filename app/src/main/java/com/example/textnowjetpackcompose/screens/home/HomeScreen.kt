package com.example.textnowjetpackcompose.screens.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.textnowjetpackcompose.navigation.DestinationScreen
import com.example.textnowjetpackcompose.screens.home.components.UsersList
import com.example.textnowjetpackcompose.viewmodels.ChatViewModels
import kotlinx.coroutines.runBlocking
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigate: (DestinationScreen) -> Unit,
    viewModel: ChatViewModels = koinViewModel()
) {
    runBlocking {
        viewModel.getUsers()

    }
    val messageText by viewModel.messageText.collectAsState()

    LaunchedEffect(messageText) {
        messageText.forEach { message ->
            Log.d("ChatScreen", "Message: Sender=${message.senderId}, Text=${message.text}")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Chats",
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.padding(top = 10.dp))

        UsersList(navigate)

    }

}


