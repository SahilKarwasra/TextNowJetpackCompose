package com.example.textnowjetpackcompose.features.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@Composable
fun ChatUserCardShimmer(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 5.dp,
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Row(
            modifier = Modifier.padding(start = 10.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile picture shimmer
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .placeholder(
                        visible = true,
                        color = Color.Gray.copy(alpha = 0.2f),
                        shape = CircleShape,
                        highlight = PlaceholderHighlight.shimmer()
                    )
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Name & message shimmer
            Column(modifier = Modifier.weight(1f)) {
                // Name shimmer
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(16.dp)
                        .placeholder(
                            visible = true,
                            color = Color.Gray.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp),
                            highlight = PlaceholderHighlight.shimmer()
                        )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Message shimmer
                Box(
                    modifier = Modifier
                        .width(180.dp)
                        .height(14.dp)
                        .placeholder(
                            visible = true,
                            color = Color.Gray.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp),
                            highlight = PlaceholderHighlight.shimmer()
                        )
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Time & unread count shimmer
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                // Time shimmer
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(14.dp)
                        .placeholder(
                            visible = true,
                            color = Color.Gray.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp),
                            highlight = PlaceholderHighlight.shimmer()
                        )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Unread count shimmer (circle)
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .placeholder(
                            visible = true,
                            color = Color.Gray.copy(alpha = 0.2f),
                            shape = CircleShape,
                            highlight = PlaceholderHighlight.shimmer()
                        )
                )
            }
        }
    }
}
