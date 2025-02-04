package com.example.textnowjetpackcompose.screens.chats

import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
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
    val listState = rememberLazyListState()

    LaunchedEffect(chatState.messages.size) {
        if (chatState.messages.isNotEmpty()) {
            listState.animateScrollToItem(chatState.messages.size - 1)
        }
    }

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
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.ime)
            .imePadding(), // Adjusts for keyboard automatically
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
                            image = null
                        )
                        try {
                            viewModels.sendMessage(userId, message)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(context, "Failed to send message", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            ChatMessagesContainer(
                modifier = Modifier.weight(1f)
                    .windowInsetsPadding(WindowInsets.ime.union(WindowInsets.systemBars)),
                messages = {
                    chatState.messages
                },
                listState = listState
            )
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatMessagesContainer(
    messages: () -> List<MessageModel>,
    modifier: Modifier = Modifier,
    listState: LazyListState,
) {
    val messagesList by remember {
        derivedStateOf { messages() }
    }
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .windowInsetsPadding(WindowInsets.ime.union(WindowInsets.systemBars)),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        state = listState,
    ) {
        items(messagesList, key = { message -> message.createdAt }) { message ->
            val preferenceManager = PreferenceManager(LocalContext.current)
            val currentUser = preferenceManager.getUser()
            val currentUserId = currentUser?.id ?: ""
            val isCurrentUser = message.senderId == currentUserId
            AnimatedVisibility(
                visible = true,
                enter = slideInHorizontally(
                    initialOffsetX = { if (isCurrentUser) it else -it },
                    animationSpec = tween(300)
                ) + fadeIn(),
                exit = shrinkOut() + fadeOut()
            ) {
                ChatBubble(
                    message, modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null)
                )
            }
        }
    }
}

@Composable
fun ChatBubble(
    message: MessageModel,
    modifier: Modifier = Modifier
) {
    val preferenceManager = PreferenceManager(LocalContext.current)
    val userResponse = preferenceManager.getUser()
    val sentByMe = message.senderId == userResponse?.id

    val bubbleColor = if (sentByMe) {
        MaterialTheme.colorScheme.surfaceContainerHigh
    } else {
        if (isSystemInDarkTheme()){
            Color(0xff7c1034)
        } else
        {
            Color(0xff4c99f3)
        }
    }

    val shape = if (sentByMe) {
        RoundedCornerShape(16.dp, 4.dp, 16.dp, 16.dp)
    } else {
        RoundedCornerShape(4.dp, 16.dp, 16.dp, 16.dp)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .align(if (sentByMe) Alignment.TopEnd else Alignment.TopStart),
            horizontalAlignment = if (sentByMe) Alignment.End else Alignment.Start
        ) {


            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = if (sentByMe) Alignment.CenterEnd else Alignment.CenterStart
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .background(bubbleColor, shape)
                        .padding(12.dp),
                    horizontalAlignment = if (sentByMe) Alignment.End else Alignment.Start
                ) {
                    message.text?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    message.image?.let { url ->
                        Spacer(modifier = Modifier.height(8.dp))
                        AsyncImage(
                            model = url,
                            contentDescription = "Attached image",
                            modifier = Modifier
                                .sizeIn(maxWidth = 250.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }

//                    Text(
//                        text = SimpleDateFormat("hh:mm a", Locale.getDefault())
//                            .format(Date(message.createdAt.toLong())),
//                        style = MaterialTheme.typography.labelSmall,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant,
//                        modifier = Modifier.padding(top = 4.dp)
//                    )
                }
            }
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
                            onSendMessage(message.trimEnd())
                            message = ""
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
                        onSendMessage(message.trim())
                        message = ""
                    }
                }
            ),
        )
    }
}

