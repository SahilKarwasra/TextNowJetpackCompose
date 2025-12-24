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
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ChatBubble(
    text: String,
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
        } else
        {
            Color(0xff4c99f3)
        }
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
                    if (isMine) {
                        val tailBaseX = right + radiusPx / 12f
                        val tailBaseY = top + radiusPx / 2.25f
                        path.moveTo(tailBaseX - tailW - 5, tailBaseY - tailH / 2.35f - 2)
                        path.lineTo(tailBaseX + tailW - 2, tailBaseY / 4 - 3)
                        path.lineTo(tailBaseX - tailW, tailBaseY + tailH  )
                        path.close()

                        drawPath(path = path, color = bubbleColor)
                    } else {
                        val tailBaseX = left - radiusPx / 8f
                        val tailBaseY = top + radiusPx / 2.35f
                        path.moveTo(tailBaseX + tailW + 5 , tailBaseY - tailH / 2.35f - 2)
                        path.lineTo(tailBaseX - tailW - 2, tailBaseY / 4 - 3)
                        path.lineTo(tailBaseX + tailW, tailBaseY + tailH)
                        path.close()
                        drawPath(path = path, color = bubbleColor)
                    }
                }
                .background(color = Color.Transparent)
                .padding(padding)
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatBubblePreview() {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        ChatBubble(
            text = "Hey — here's a left bubble (incoming) with a tail. It automatically wraps long text.",
            isMine = false
        )
        ChatBubble(
            text = "This is my outgoing message. Tail on right.",
            isMine = true
        )
    }
}
