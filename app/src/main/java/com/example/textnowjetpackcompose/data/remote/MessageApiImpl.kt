package com.example.textnowjetpackcompose.data.remote

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.textnowjetpackcompose.data.HttpRoutes
import com.example.textnowjetpackcompose.data.model.MessageModel
import com.example.textnowjetpackcompose.data.model.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.io.IOException

class MessageApiImpl(
    private val client: HttpClient,
    private val dataStore: DataStore<Preferences>
) : MessageApi {


    private val authTokenKey = stringPreferencesKey("auth_token")

    override suspend fun getUsers(): List<UserResponse> {
        val token = dataStore.data.firstOrNull()?.get(authTokenKey)
        Log.d("MessageApiImpl", "getUsers: Token from dataStore: $token")
        val rawToken = token?.substringBefore(";")
        Log.d("", "getUsers: Raw Cookie from DataStore: $rawToken")
        val response: HttpResponse = client.get(HttpRoutes.getUsers) {
            headers {
                append(HttpHeaders.Cookie, "$rawToken")
            }
        }
        Log.d("MessageApiImpl", "getUsers: Response status: ${response.bodyAsText()}")
        if (!response.status.isSuccess()) {
            val errorBody = response.body<String>()
            Log.e("MessageApiImpl", "getUsers: HTTP error: ${response.status}, body: $errorBody")
            throw IOException("HTTP error: ${response.status}, body: $errorBody")
        }
        return response.body<List<UserResponse>>()
    }

    override suspend fun getMessages(receiverId: String): List<MessageModel> {
        val token = dataStore.data.firstOrNull()?.get(authTokenKey)
        val rawToken = token?.substringBefore(";")
        Log.d("getMessages", "Token from dataStore: $token")
        try {
            val response: HttpResponse = client.get("${HttpRoutes.getMessages}$receiverId") {
                headers {
                    append(HttpHeaders.Cookie, "$rawToken")
                }
            }
            if (response.status == HttpStatusCode.OK) {
                Log.d("getMessages", "Response status: ${response.status}")
                return response.body<List<MessageModel>>()
            } else {
                Log.e("getMessages", "Error fetching messages: ${response.status}")
                error("Failed to fetch messages: ${response.status}")
            }
        } catch (e: Exception) {
            Log.e("getMessages", "Error fetching messages: ${e.message}")
            throw e
        }
    }

    override suspend fun sendMessage(
        receiverId: String,
        messageModel: MessageModel
    ): HttpResponse {
        val token = dataStore.data.firstOrNull()?.get(authTokenKey)
        val rawToken = token?.substringBefore(";")
        try {
            val response = client.post("${HttpRoutes.sendMessages}$receiverId") {
                headers {
                    append(HttpHeaders.Cookie, "$rawToken")
                }
                contentType(ContentType.Application.Json)
                setBody(messageModel)
            }
            return response
        } catch (e: Exception) {
            Log.e("sendMessage", "Error sending message: ${e.message}")
            throw e
        }
    }

    override suspend fun generateText(prompt: String): String {

        return withContext(Dispatchers.IO){
            try {
                val response = client.post(HttpRoutes.generateText) {
                    contentType(ContentType.Application.Json)
                    setBody(mapOf("message" to prompt))
                }
                val responseBody = response.body<Map<String, String>>() // If the response is simply a map of strings
                val aiResponse = responseBody["response"] as? String
                aiResponse?.trim() ?: throw Exception("No response received from server")

            } catch (e: Exception) {
                Log.e("generateText", "Error generating text: ${e.message}")
                throw Exception("Failed to generate message: ${e.message}")
            }
        }
    }
}