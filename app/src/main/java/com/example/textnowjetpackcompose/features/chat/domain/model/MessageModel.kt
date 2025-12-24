package com.example.textnowjetpackcompose.features.chat.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Serializable
data class MessageRequest(
    val senderId: String,
    val receiverId: String,
    val text: String? = null,
    val image: String? = null,
    val createdAt: String,
    val updatedAt: String? = null
)

@Serializable
data class MessageModel(
    val _id: String,
    val senderId: String,
    val receiverId: String,
    val text: String? = null,
    val image: String? = null,
    val createdAt: String,
    val updatedAt: String? = null
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    val id: String,
    val senderId: String,
    val receiverId: String,
    val text: String? = null,
    val image: String? = null,
    val createdAt: String,
    val updatedAt: String? = null,
    val status: MessageStatus = MessageStatus.SENT // optional: SENT, DELIVERED, FAILED
)

enum class MessageStatus {
    SENT, DELIVERED, FAILED
}

fun MessageModel.toMessageEntity(): MessageEntity {
    return MessageEntity(
        id = _id,
        senderId = senderId,
        receiverId = receiverId,
        text = text,
        image = image,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}