package com.example.textnowjetpackcompose.screens.chats

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.textnowjetpackcompose.data.model.MessageModel
import com.example.textnowjetpackcompose.navigation.DestinationScreen
import com.example.textnowjetpackcompose.screens.utils.PreferenceManager
import com.example.textnowjetpackcompose.viewmodels.AuthViewModel
import com.example.textnowjetpackcompose.viewmodels.ChatViewModels
import kotlinx.coroutines.runBlocking
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    userName: String,
    navigateToHomeScreen: (DestinationScreen) -> Unit,
    userId: String,
    viewModels: ChatViewModels = koinViewModel()
) {

    val messages = viewModels.messageText.collectAsState(initial = emptyList()).value

    runBlocking {
        viewModels.getMessages(userId)
        Log.d("ChatScreen", "ChatScreen: ${viewModels.messageText.value}")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        userName,
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigateToHomeScreen(DestinationScreen.HomeScreenObj)
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                },
                windowInsets = WindowInsets.ime,
            )
        },
        bottomBar = {
            SendMessageTextField(
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    ) { innerPadding ->
        ChatMessagesContainer(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            messages = messages
        )

    }

}

@Composable
fun ChatMessagesContainer(
    messages: List<MessageModel>,
    modifier: Modifier = Modifier) {
//    val dummyMessages = remember {
//        listOf(
//            MessageItem("Hi there!  ", "10:00 AM", isSentByMe = true),
//            MessageItem("Hello! How are you?", "10:01 AM", isSentByMe = false),
//            MessageItem("I'm doing great, thanks!", "10:02 AM", isSentByMe = true),
//            MessageItem("That's awesome to hear!", "10:03 AM", isSentByMe = false)
//        )
//    }

    LazyColumn(
        modifier = modifier.padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(messages) { message -> // Use messages list
            ChatBubble(message)
        }
    }
}

@Composable
fun ChatBubble(message: MessageModel, ) {

    val preferenceManager = PreferenceManager(context = LocalContext.current)

    val userResponse = preferenceManager.getUser()
    Log.d("ChatBubble", "ChatBubble: ${userResponse?.id}")
    val sentByMe = message.senderId == userResponse?.id

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = if (sentByMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {

        Column(
            modifier = Modifier
                .padding(8.dp)
                .background(
                    if (isSystemInDarkTheme()) MaterialTheme.colorScheme.inverseOnSurface
                    else {
                        if (sentByMe) MaterialTheme.colorScheme.inverseOnSurface
                        else MaterialTheme.colorScheme.primaryContainer
                    },
                    shape = if (sentByMe) RoundedCornerShape(
                        topStart = 24.dp,
                        topEnd = 24.dp,
                        bottomStart = 24.dp
                    ) else RoundedCornerShape(
                        topStart = 24.dp,
                        topEnd = 24.dp,
                        bottomEnd = 24.dp
                    )
                )
                .padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            message.text?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            message.image?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "",
                    modifier = Modifier.size(150.dp),
                    )
            }
            message.createdAt?.let {
                Text(
                    text = it,
                    fontSize = 10.sp,
                    textAlign = TextAlign.End
                )
            }
        }

    }
}


@Composable
fun SendMessageTextField(modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        TextField(
            value = "",
            onValueChange = {},
            placeholder = {
                Text(text = "Search")
            },
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorContainerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            modifier = Modifier
                .fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = {
//                    TODO
                }) {
                    Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = "")
                }
            }
        )
    }
}

