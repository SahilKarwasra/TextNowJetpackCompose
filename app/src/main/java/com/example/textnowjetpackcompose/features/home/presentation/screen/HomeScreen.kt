package com.example.textnowjetpackcompose.features.home.presentation.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.textnowjetpackcompose.config.PreferenceManager
import com.example.textnowjetpackcompose.features.auth.presentation.viewmodel.AuthState
import com.example.textnowjetpackcompose.features.auth.presentation.viewmodel.AuthViewModel
import com.example.textnowjetpackcompose.config.navigation.DestinationScreen
import com.example.textnowjetpackcompose.features.home.presentation.components.ChatUserCard
import com.example.textnowjetpackcompose.features.home.presentation.components.ChatUserCardShimmer
import com.example.textnowjetpackcompose.features.home.presentation.viewmodel.HomeScreenState
import com.example.textnowjetpackcompose.features.home.presentation.viewmodel.HomeScreenViewModel
import com.example.textnowjetpackcompose.features.shared.BottomNavigation

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    navigate: (DestinationScreen) -> Unit,
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel,
) {

    val homeState by homeScreenViewModel.state.collectAsStateWithLifecycle()
    val lazyColumnState = rememberLazyListState()
    val currentUser = homeScreenViewModel.currentUser.value

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
                    val messages = (homeState as HomeScreenState.Success).messages
                    items(messages.size) { index ->
                        val user = messages[index]
                        ChatUserCard(
                            profilePicUrl = user.profilePic,
                            name = user.fullName,
                            message = user.lastMessage?.text ?: "",
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