package com.example.textnowjetpackcompose.features.chat.data.remote

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.textnowjetpackcompose.config.HttpRoutes
import com.example.textnowjetpackcompose.features.chat.domain.model.MessageModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class ChatApi(
    private val client: HttpClient,
    private val dataStore: DataStore<Preferences>
) {

    private val authTokenKey = stringPreferencesKey("auth_token")

    suspend fun getMessages(receiverId: String): List<MessageModel> {
        val token = dataStore.data.firstOrNull()?.get(authTokenKey)
        val rawToken = token?.substringBefore(";")

        return withContext(Dispatchers.IO) {
            try {
                val response = client.get("${HttpRoutes.getMessages}$receiverId") {
                    headers {
                        append(HttpHeaders.Cookie, "$rawToken")
                    }
                }
                response.body()
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
    }

    suspend fun sendMessage(receiverId: String, messageModel: MessageModel): MessageModel {
        val token = dataStore.data.firstOrNull()?.get(authTokenKey)
        val rawToken = token?.substringBefore(";")

        return withContext(Dispatchers.IO) {
            try {
                val response = client.post("${HttpRoutes.sendMessages}$receiverId") {
                    headers {
                        append(HttpHeaders.Cookie, "$rawToken")
                    }
                    contentType(ContentType.Application.Json)
                    setBody(messageModel)
                }
                response.body()
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
    }
}



















