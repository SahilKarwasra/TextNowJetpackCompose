package com.example.textnowjetpackcompose.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.textnowjetpackcompose.R
import kotlin.text.format
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatBox(
    modifier: Modifier = Modifier,
    avatarResId: Int, // Dynamic avatar resource
    name: String, // Dynamic name
    message: String, // Dynamic message
    time: String, // Dynamic time
    unreadCount: Int = 0 // Dynamic unread count, default is 0
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(vertical = 4.dp), // Minimal padding between list items
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
                Image(
                    painter = painterResource(id = avatarResId),
                    contentDescription = null,
                    Modifier.size(70.dp)
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
                horizontalAlignment = Alignment.End // Align everything to the right
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
fun ChatList(scrollState: LazyListState) {
    val chats = listOf(
        ChatData(
            avatarResId = R.drawable.avatar,
            name = "Mr. Ninja Hattori",
            message = "Hello, How are you?",
            time = "12:34",
            unreadCount = 2
        ),
        ChatData(
            avatarResId = R.drawable.avatar,
            name = "Doraemon",
            message = "Let's meet tomorrow!",
            time = "10:45",
            unreadCount = 0
        ),
        ChatData(
            avatarResId = R.drawable.avatar,
            name = "Shinchan",
            message = "See you at the party!",
            time = "9:30",
            unreadCount = 5
        ),ChatData(
            avatarResId = R.drawable.avatar,
            name = "Mr. Ninja Hattori",
            message = "Hello, How are you?",
            time = "12:34",
            unreadCount = 2
        ),
        ChatData(
            avatarResId = R.drawable.avatar,
            name = "Doraemon",
            message = "Let's meet tomorrow!",
            time = "10:45",
            unreadCount = 0
        ),
        ChatData(
            avatarResId = R.drawable.avatar,
            name = "Shinchan",
            message = "See you at the party!",
            time = "9:30",
            unreadCount = 5
        ),ChatData(
            avatarResId = R.drawable.avatar,
            name = "Mr. Ninja Hattori",
            message = "Hello, How are you?",
            time = "12:34",
            unreadCount = 2
        ),
        ChatData(
            avatarResId = R.drawable.avatar,
            name = "Doraemon",
            message = "Let's meet tomorrow!",
            time = "10:45",
            unreadCount = 0
        ),
        ChatData(
            avatarResId = R.drawable.avatar,
            name = "Shinchan",
            message = "See you at the party!",
            time = "9:30",
            unreadCount = 5
        ),
        ChatData(
            avatarResId = R.drawable.avatar,
            name = "Mr. Ninja Hattori",
            message = "Hello, How are you?",
            time = "12:34",
            unreadCount = 2
        ),
        ChatData(
            avatarResId = R.drawable.avatar,
            name = "Doraemon",
            message = "Let's meet tomorrow!",
            time = "10:45",
            unreadCount = 0
        ),
        ChatData(
            avatarResId = R.drawable.avatar,
            name = "Shinchan",
            message = "See you at the party!",
            time = "9:30",
            unreadCount = 5
        ),
        ChatData(
            avatarResId = R.drawable.avatar,
            name = "Mr. Ninja Hattori",
            message = "Hello, How are you?",
            time = "12:34",
            unreadCount = 2
        ),
        ChatData(
            avatarResId = R.drawable.avatar,
            name = "Doraemon",
            message = "Let's meet tomorrow!",
            time = "10:45",
            unreadCount = 0
        ),
        ChatData(
            avatarResId = R.drawable.avatar,
            name = "Shinchan",
            message = "See you at the party!",
            time = "9:30",
            unreadCount = 5
        ),
    )

    LazyColumn(state = scrollState,
        modifier = Modifier.fillMaxSize(),
    ) {

        items(chats) { chat ->
            ChatBox(
                avatarResId = chat.avatarResId,
                name = chat.name,
                message = chat.message,
                time = chat.time,
                unreadCount = chat.unreadCount
            )
        }
    }
}

data class ChatData(
    val avatarResId: Int,
    val name: String,
    val message: String,
    val time: String,
    val unreadCount: Int
)


