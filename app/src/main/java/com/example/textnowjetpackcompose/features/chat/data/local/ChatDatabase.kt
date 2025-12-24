//package com.example.textnowjetpackcompose.features.chat.data.local
//
//import androidx.room.Database
//import androidx.room.RoomDatabase
//import com.example.textnowjetpackcompose.features.chat.data.local.dao.MessageDao
//import com.example.textnowjetpackcompose.features.chat.domain.model.MessageEntity
//
//
//@Database(entities = [MessageEntity::class], version = 1, exportSchema = false)
//abstract class ChatDatabase: RoomDatabase() {
//    abstract fun messageDao(): MessageDao
//}