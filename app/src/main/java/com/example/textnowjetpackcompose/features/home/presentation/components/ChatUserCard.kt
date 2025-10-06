package com.example.textnowjetpackcompose.features.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.textnowjetpackcompose.R

@Composable
fun ChatUserCard(
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
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                onClick()
            },
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
                        .fillMaxWidth(0.7f),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
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
