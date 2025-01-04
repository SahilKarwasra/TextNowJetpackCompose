package com.example.textnowjetpackcompose.screens.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.example.textnowjetpackcompose.navigation.DestinationScreen
import com.example.textnowjetpackcompose.screens.home.components.ChatBox
import com.example.textnowjetpackcompose.screens.home.components.ChatList
import com.example.textnowjetpackcompose.screens.home.components.SearchBoxForChats
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
    val scrollState = rememberLazyListState()
    val isSearchBoxVisible = remember { mutableStateOf(true) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // Check if we are scrolling vertically
                if (available.y < 0) { // Scrolling down
                    if (isSearchBoxVisible.value) {
                        isSearchBoxVisible.value = false
                    }
                } else if (available.y > 0) { // Scrolling up
                    if (!isSearchBoxVisible.value) {
                        isSearchBoxVisible.value = true
                    }
                }
                return super.onPreScroll(available, source)
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(horizontal = 20.dp)
            .nestedScroll(nestedScrollConnection)
    ) {
        Text(
            text = "Chats",
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.padding(top = 10.dp))
        AnimatedVisibility(
            visible = isSearchBoxVisible.value,
            enter = slideInVertically(
                animationSpec = tween(durationMillis = 300),
                initialOffsetY = { -it } // Slide in from the top
            ),
            exit = slideOutVertically(
                animationSpec = tween(durationMillis = 300),
                targetOffsetY = { -it } // Slide out to the top
            )
        ) {
            SearchBoxForChats()
        }
        Spacer(modifier = Modifier.padding(top = 13.dp))
        ChatList(scrollState = scrollState)

    }

    Log.d("HomeScreen", "HomeScreen: ${viewModel.Users}")
}


