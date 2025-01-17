package com.example.textnowjetpackcompose.screens.chats

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.textnowjetpackcompose.viewmodels.ChatState
import com.example.textnowjetpackcompose.viewmodels.ChatViewModels
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    receiverName: String,
    navigateToHomeScreen: (DestinationScreen) -> Unit,
    userId: String,
    viewModels: ChatViewModels = koinViewModel()
) {
    val chatState by viewModels.chatState.collectAsState()
    val preferenceManager = PreferenceManager(LocalContext.current)
    val senderId = preferenceManager.getUser()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    // Initialize socket on launch
    LaunchedEffect(Unit) {
        viewModels.initializeSocket(senderId?.id ?: "")
        viewModels.getMessages(userId)
    }

    // Disconnect socket when leaving screen
    DisposableEffect(Unit) {
        onDispose {
            viewModels.disconnectSocket()
        }
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        receiverName,
                        style = MaterialTheme.typography.displaySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                scrollBehavior = topAppBarScrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { navigateToHomeScreen(DestinationScreen.HomeScreenObj) }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                },
            )
        },
        bottomBar = {
            SendMessageTextField(
                modifier = Modifier.padding(16.dp),
                onSendMessage = { messageText ->
                    scope.launch {
                        val currentTime = System.currentTimeMillis().toString()
                        val message = MessageModel(
                            text = messageText,
                            senderId = senderId?.id ?: "",
                            receiverId = userId,
                            createdAt = currentTime,
                            updatedAt = "",
                            image = ""
                        )
                        try {
                            viewModels.sendMessage(userId, message)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(context, "Failed to send message", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }
    ) {
        ChatMessagesContainer(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            messages = {
                chatState.messages
            }
        )
    }
}

@Composable
fun ChatMessagesContainer(
    messages: () -> List<MessageModel>, modifier: Modifier = Modifier
) {
    val messagesList by remember{
        derivedStateOf { messages() }
    }
    LazyColumn(
        modifier = modifier.padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(messagesList, key = { message -> message.createdAt}) { message ->
            ChatBubble(message)
        }
    }
}

@Composable
fun ChatBubble(message: MessageModel) {
    val preferenceManager = PreferenceManager(LocalContext.current)
    val userResponse = preferenceManager.getUser()
    val sentByMe = message.senderId == userResponse?.id

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if (sentByMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .background(
                    color = if (sentByMe) MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    else MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                    shape = if (sentByMe) RoundedCornerShape(
                        topStart = 24.dp, topEnd = 24.dp, bottomStart = 24.dp
                    ) else RoundedCornerShape(
                        topStart = 24.dp, topEnd = 24.dp, bottomEnd = 24.dp
                    )
                )
                .padding(12.dp)
        ) {
            message.text?.let {
                Text(text = it, style = MaterialTheme.typography.titleLarge)
            }
            Spacer(modifier = Modifier.height(4.dp))
            message.image?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Attached image",
                    modifier = Modifier.size(150.dp),
                )
            }
//            message.createdAt?.let {
//                val formattedTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(it.toLong()))
//                Text(
//                    text = formattedTime,
//                    fontSize = 10.sp,
//                    textAlign = TextAlign.End
//                )
//            }
        }
    }
}

@Composable
fun SendMessageTextField(
    modifier: Modifier = Modifier,
    onSendMessage: (String) -> Unit
) {
    Surface(
        modifier = modifier.padding(horizontal = 20.dp)
    ) {
        var message by remember { mutableStateOf("") }

        TextField(
            value = message,
            onValueChange = { message = it },
            placeholder = { Text(text = "Type your message...") },
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (message.isNotBlank()) {
                            onSendMessage(message)
                            message = ""
                        }
                    },
                    enabled = message.isNotBlank()
                ) {
                    Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = "Send")
                }
            }
        )
    }
}
