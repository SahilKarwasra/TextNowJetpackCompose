package com.example.textnowjetpackcompose.screens.home.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.textnowjetpackcompose.R
import com.example.textnowjetpackcompose.navigation.DestinationScreen
import com.example.textnowjetpackcompose.viewmodels.ChatViewModels
import org.koin.androidx.compose.koinViewModel

@Composable
fun UsersBox(
    modifier: Modifier = Modifier,
    profilePicUrl: String?,
    name: String,
    message: String,
    time: String,
    unreadCount: Int = 0,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(vertical = 4.dp)
            .clickable {
                onClick()
            }
        ,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 5.dp,
        color = MaterialTheme.colorScheme.surfaceContainer,

    ) {
        Row {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 10.dp),
                verticalArrangement = Arrangement.Center
            ) {

//                Log.d("ChatBox", "ChatBox: $profilePicUrl")
                AsyncImage(
                    model = profilePicUrl,
                    contentDescription = "",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    placeholder = painterResource(R.drawable.avatar),
                    error = painterResource(R.drawable.avatar)
                )

            }
            Column(
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 18.dp)
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Thin,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier
                    .padding(end = 20.dp, top = 18.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = time,
                    style = MaterialTheme.typography.labelMedium,
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .background(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                            .size(25.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = unreadCount.toString(),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun UsersList( navigate: (DestinationScreen) -> Unit) {

    val viewModel: ChatViewModels = koinViewModel()

    val state by viewModel.chatState.collectAsState()

    when{
        state.isLoading -> {
            CircularProgressIndicator()
        }
        state.errorMessage != null -> {
            Text(text = state.errorMessage ?: "Unknown Error")
        }
        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                item {
                    SearchBoxForChats()
                }
                item {
                    Spacer(modifier = Modifier.padding(top = 13.dp))
                }
                items(state.users) { user ->
                    val lastMessage = state.lastMessages[user.id] ?: ""
                    UsersBox(
                        profilePicUrl = user.profilePic,
                        name = user.fullName,
                        message = lastMessage,
                        time = state.messages.lastOrNull { it.receiverId == user.id }?.createdAt ?: "",
                        onClick = {
                            navigate(DestinationScreen.ChatScreenObj(user.id,user.fullName))
                        }
                    )
                }
            }
        }
    }
}

