package com.example.textnowjetpackcompose.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.textnowjetpackcompose.navigation.DestinationScreen
import com.example.textnowjetpackcompose.screens.home.components.UsersList
import com.example.textnowjetpackcompose.viewmodels.ChatViewModels
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigate: (DestinationScreen) -> Unit,
    viewModel: ChatViewModels = koinViewModel()
) {

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


