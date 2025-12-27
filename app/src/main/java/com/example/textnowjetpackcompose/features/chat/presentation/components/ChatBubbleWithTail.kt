package com.example.textnowjetpackcompose.features.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatBubble(
    text: String,
    timestamp: Long,
    showTail: Boolean = true,
    modifier: Modifier = Modifier,
    isMine: Boolean = false,
    padding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
) {
    val density = LocalDensity.current
    val tailW = with(density) { 10.dp.toPx() }
    val tailH = with(density) { 10.dp.toPx() }
    val radiusPx = with(density) { 12.dp.toPx() }
    val bubbleColor = if (isMine) {
        MaterialTheme.colorScheme.surfaceContainerHigh
    } else {
        if (isSystemInDarkTheme()){
            Color(0xff7c1034)
        } else {
            Color(0xff4c99f3)
        }
    }

    val timeString = remember(timestamp) {
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        sdf.format(Date(timestamp))
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .wrapContentWidth()
                .drawBehind {
                    val left = 0f
                    val top = 0f
                    val right = size.width
                    val bottom = size.height

                    val rect = Rect(left, top, right, bottom)
                    drawRoundRect(
                        color = bubbleColor,
                        topLeft = Offset(rect.left, rect.top),
                        size = Size(rect.width, rect.height),
                        cornerRadius = CornerRadius(radiusPx, radiusPx)
                    )

                    val path = Path()
                    if (showTail) {
                        if (isMine) {
                            val tailBaseX = right + radiusPx / 12f
                            val tailBaseY = top + radiusPx / 2.25f
                            path.moveTo(tailBaseX - tailW - 5, tailBaseY - tailH / 2.35f - 2)
                            path.lineTo(tailBaseX + tailW - 2, tailBaseY / 4 - 3)
                            path.lineTo(tailBaseX - tailW, tailBaseY + tailH)
                            path.close()

                            drawPath(path = path, color = bubbleColor)
                        } else {
                            val tailBaseX = left - radiusPx / 8f
                            val tailBaseY = top + radiusPx / 2.35f
                            path.moveTo(tailBaseX + tailW + 5, tailBaseY - tailH / 2.35f - 2)
                            path.lineTo(tailBaseX - tailW - 2, tailBaseY / 4 - 3)
                            path.lineTo(tailBaseX + tailW, tailBaseY + tailH)
                            path.close()
                            drawPath(path = path, color = bubbleColor)
                        }

                    }
                }
                .background(color = Color.Transparent)
                .padding(padding)
        ) {
            Row(
                modifier = Modifier.wrapContentWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = TextStyle(fontSize = 15.sp),
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .padding(end = 4.dp)
                )

                Text(
                    text = timeString,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatBubblePreview() {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        ChatBubble(
            text = "Hey there!",
            timestamp = System.currentTimeMillis(),
            isMine = false
        )
        Spacer(modifier = Modifier.height(8.dp))
        ChatBubble(
            text = "This is a longer message that wraps to multiple lines and the timestamp should stay in the bottom right corner",
            timestamp = System.currentTimeMillis() - 60000,
            isMine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        ChatBubble(
            text = "Short",
            timestamp = System.currentTimeMillis() - 120000,
            isMine = false
        )
    }
}