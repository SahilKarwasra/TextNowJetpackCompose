package com.example.textnowjetpackcompose.data.remote

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.textnowjetpackcompose.data.HttpRoutes
import com.example.textnowjetpackcompose.data.model.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.firstOrNull
import java.io.IOException

class MessageApiImpl(private val client: HttpClient,private val dataStore: DataStore<Preferences>): MessageApi {


    private val authTokenKey = stringPreferencesKey("auth_token")

    override suspend fun getUsers(): List<UserResponse> {
        val token = dataStore.data.firstOrNull()?.get(authTokenKey)
        Log.d("MessageApiImpl", "getUsers: Token from dataStore: $token")
        val rawToken = token?.substringBefore(";")
        Log.d("", "getUsers: Raw Cookie from DataStore: $rawToken")
        val response: HttpResponse = client.get(HttpRoutes.getUsers){
            headers {
                append(HttpHeaders.Cookie, "$rawToken")
            }
        }
        Log.d("MessageApiImpl", "getUsers: Response status: ${response}")
        if (!response.status.isSuccess()) {
            val errorBody = response.body<String>()
            Log.e("MessageApiImpl", "getUsers: HTTP error: ${response.status}, body: $errorBody")
            throw IOException("HTTP error: ${response.status}, body: $errorBody")
        }
        return response.body<List<UserResponse>>()
    }
}