package com.example.textnowjetpackcompose.features.shared

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DataUsage
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.textnowjetpackcompose.config.navigation.DestinationScreen

@Composable
fun BottomNavigation(
    modifier: Modifier = Modifier,
    navController: NavController,
    navigate: (DestinationScreen) -> Unit
) {

    val items = listOf(
        BottomNavItem.Chat,
        BottomNavItem.Status,
        BottomNavItem.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    BottomAppBar(
        modifier = Modifier.clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        items.forEach { item ->
            val currentDestination = navBackStackEntry?.destination
            val isSelected = currentDestination?.hierarchy?.any {
                it.hasRoute(item.obj::class)
            } == true
            NavigationBarItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.title,
                        modifier = Modifier.size(30.dp)
                    )
                },
                selected = isSelected,
                onClick = {
                    navigate(item.screen)
                },
            )
        }
    }
}

sealed class BottomNavItem(
    val obj: Any,
    val title: String,
    val icon: ImageVector,
    val screen: DestinationScreen
) {
    object Chat : BottomNavItem(
        DestinationScreen.HomeScreenObj, "Chat",
        Icons.Outlined.Home, DestinationScreen.HomeScreenObj
    )

    object Status : BottomNavItem(
        DestinationScreen.AiBotScreenObj, "Status",
        Icons.Outlined.DataUsage, DestinationScreen.AiBotScreenObj
    )

    object Profile : BottomNavItem(
        DestinationScreen.ProfileScreenObj, "Settings",
        Icons.Outlined.Person, DestinationScreen.ProfileScreenObj
    )
}