package com.example.textnowjetpackcompose.screens.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.textnowjetpackcompose.navigation.DestinationScreen

@Composable
fun BottomNavigationBar(navigate: (DestinationScreen) -> Unit
) {
    val items = listOf(
        BottomNavItem.Chat,
        BottomNavItem.Status,
        BottomNavItem.Profile
    )
    BottomAppBar(
        modifier = Modifier.clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
        contentPadding = PaddingValues(
            top = 20.dp
        ),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                selected = false, // You'll need to handle selection differently
                onClick = {
                    navigate(item.screen)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSurface,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    indicatorColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    disabledIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                )
            )
        }
    }
}

sealed class BottomNavItem(val title: String, val icon: ImageVector, val screen: DestinationScreen) {
    object Chat : BottomNavItem("Chat",
        Icons.AutoMirrored.Filled.Chat, DestinationScreen.HomeScreenObj)
    object Status : BottomNavItem("Status",
        Icons.Filled.DataUsage, DestinationScreen.StatusScreenObj)
    object Profile : BottomNavItem("Settings",
        Icons.Filled.Person, DestinationScreen.ProfileScreenObj)
}
