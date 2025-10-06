package com.example.textnowjetpackcompose.features.chat.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChatScreenTopBar(
    receiverName: String,
    onBackIconClick: () -> Unit,
    onMenuIconClick: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.padding(horizontal = 16.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = receiverName,
                    maxLines = 1,
                    style = MaterialTheme.typography.headlineMediumEmphasized
                )
            }
        },
        navigationIcon = {
            Icon(
                Icons.Default.ArrowBackIosNew,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(
                        onClick = onBackIconClick
                    )
            )
        },
        actions = {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = null,
                modifier = Modifier.clip(CircleShape)
                    .clickable(
                        onClick = onMenuIconClick
                    )
            )
        }
    )
}