//package com.example.textnowjetpackcompose.features.chat.data.local.dao
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import com.example.textnowjetpackcompose.features.chat.domain.model.MessageEntity
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface MessageDao {
//
//    @Query("SELECT * FROM messages WHERE receiverId = :receiverId ORDER BY createdAt ASC")
//    fun getMessages(receiverId: String): Flow<List<MessageEntity>>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertMessage(message: MessageEntity)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertMessages(messages: List<MessageEntity>)
//
//
//}