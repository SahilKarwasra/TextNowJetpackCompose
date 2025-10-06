package com.example.textnowjetpackcompose.features.home.data.remote

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.textnowjetpackcompose.config.HttpRoutes
import com.example.textnowjetpackcompose.features.home.domain.model.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.text.substringBefore

class HomeApi (
    private val client: HttpClient,
    private val dataStore: DataStore<Preferences>
) {

    private val authTokenKey = stringPreferencesKey("auth_token")


    suspend fun getUsers(): List<UserResponse> {
        var token = dataStore.data.firstOrNull()?.get(authTokenKey)
        while (token.isNullOrEmpty()){
            delay(500)
            token = dataStore.data.firstOrNull()?.get(authTokenKey)
        }
        val rawToken = token.substringBefore(";")
        return withContext(Dispatchers.IO) {
            val response: HttpResponse = client.get(HttpRoutes.getUsers) {
                headers {
                    append(HttpHeaders.Cookie, rawToken)
                }
            }
            if (!response.status.isSuccess()) {
                val errorBody = response.body<String>()
                throw IOException("HTTP error: ${response.status}, body: $errorBody")
            }
            response.body<List<UserResponse>>()
        }
    }
}