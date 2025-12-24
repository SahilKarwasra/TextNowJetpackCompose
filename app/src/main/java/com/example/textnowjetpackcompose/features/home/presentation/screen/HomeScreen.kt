package com.example.textnowjetpackcompose.features.home.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.textnowjetpackcompose.config.navigation.DestinationScreen
import com.example.textnowjetpackcompose.features.auth.presentation.viewmodel.AuthViewModel
import com.example.textnowjetpackcompose.features.chat.presentation.viewmodel.ChatViewModel
import com.example.textnowjetpackcompose.features.home.presentation.components.ChatUserCard
import com.example.textnowjetpackcompose.features.home.presentation.components.ChatUserCardShimmer
import com.example.textnowjetpackcompose.features.home.presentation.viewmodel.HomeScreenState
import com.example.textnowjetpackcompose.features.home.presentation.viewmodel.HomeScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navigate: (DestinationScreen) -> Unit,
    homeViewModel: HomeScreenViewModel,
    chatViewModel: ChatViewModel
) {

    val homeState by homeViewModel.state.collectAsStateWithLifecycle()
    val lazyColumnState = rememberLazyListState()
    val currentUser by homeViewModel.currentUser


    LaunchedEffect(homeState) {
        if (homeState is HomeScreenState.Success) {
            val users = (homeState as HomeScreenState.Success).users
            if (users.isNotEmpty()) {
                users.forEach {
                    chatViewModel.loadMessages(it.id)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(horizontal = 18.dp)
    ) {
        Text(
            "Chats",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Surface(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
            tonalElevation = 30.dp,
            shape = RoundedCornerShape(20.dp),
            shadowElevation = 5.dp
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
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.Search, contentDescription = "")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.padding(top = 10.dp))

        LazyColumn(
            state = lazyColumnState
        ) {
            when (homeState) {
                is HomeScreenState.Loading -> {
                    item {
                        repeat(3) {
                            ChatUserCardShimmer()
                        }
                    }
                }

                is HomeScreenState.Error -> {
                    item {
                        Text(text = (homeState as HomeScreenState.Error).message)
                    }
                }

                is HomeScreenState.Success -> {
                    val users = (homeState as HomeScreenState.Success).users
                    items(users.size) { index ->
                        val user = users[index]
                        ChatUserCard(
                            profilePicUrl = user.profilePic,
                            name = user.fullName,
                            message = user.lastMessage?.text ?: "No Messages Yet!!",
                            time = user.lastMessage?.createdAt ?: "",
                            unreadCount = 0
                        ) {
                            currentUser?.id?.let { navigate(DestinationScreen.ChatScreenObj(user.fullName, it, user.id)) }
                        }
                    }
                }

                else -> {}
            }
        }

    }
}