package com.example.textnowjetpackcompose.features.auth.data.remote

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.textnowjetpackcompose.config.HttpRoutes
import com.example.textnowjetpackcompose.features.auth.domain.model.LoginRequest
import com.example.textnowjetpackcompose.features.auth.domain.model.SignupRequest
import com.example.textnowjetpackcompose.features.home.domain.model.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class AuthApi(
    private val client: HttpClient,
    private val dataStore: DataStore<Preferences>
) {

    private val authTokenKey = stringPreferencesKey("auth_token")

    suspend fun signUp(request: SignupRequest): UserResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response = client.post(HttpRoutes.signup) {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
                Log.d("AuthApiImpl", "signup: Response status: ${response.status}")
                Log.d("AuthApiImpl", "signup: Response body: ${response.bodyAsText()}")
                val token = response.headers[HttpHeaders.SetCookie]
                Log.d("Cookie Signup", "signup: Set-Cookie header: $token")
                if (token != null) {
                    dataStore.updateData { settings ->
                        settings.toMutablePreferences().apply {
                            this[authTokenKey] = token
                        }
                    }
                } else {
                    Log.e("AuthApiImpl", "signup: Token is null in Set-Cookie header")
                }
                response.body()
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
    }

    suspend fun login(request: LoginRequest): UserResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response = client.post(HttpRoutes.login) {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
                val token = response.headers[HttpHeaders.SetCookie]

                Log.d("Cookie Login", "login: Set-Cookie header: $token")
                if (token != null) {
                    dataStore.edit { settings ->
                        settings[authTokenKey] = token
                        Log.d("Cookie Login", "login: Cookie stored in DataStore")
                    }
                } else {
                    Log.e("Cookie Login", "login: Token is null in Set-Cookie header")
                }
                response.body()
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
    }

    suspend fun checkAuth(): UserResponse {
        val token = dataStore.data.firstOrNull()?.get(authTokenKey)
        Log.d("CheckAuth", "checkAuth: Token from dataStore: $token")
        val rawToken = token?.substringBefore(";")
        Log.d("CheckAuth", "checkAuth: Raw Cookie from DataStore: $rawToken")

        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = client.get(HttpRoutes.checkAuth) {
                    headers {
                        append(HttpHeaders.Cookie, "$rawToken")
                    }
                }
                Log.d("CheckAuth", "checkAuth: Response status: ${response.status}")
                response.body()
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
    }

    suspend fun logout(): HttpResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response = client.post(HttpRoutes.logout)
                Log.d("Logout AuthApiImpl", "logout: Response status: ${response.status}")
                dataStore.updateData { settings ->
                    settings.toMutablePreferences().apply { remove(authTokenKey) }
                }
                response
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
    }

    suspend fun updateProfile(bytes: ByteArray): HttpResponse {
        val token = dataStore.data.firstOrNull()?.get(authTokenKey)
        val rawToken = token?.substringBefore(";")

        return withContext(Dispatchers.IO) {
            try {
                val file = File.createTempFile("profile", ".jpg")
                file.writeBytes(bytes)
                val response: HttpResponse = client.put(HttpRoutes.updateProfile) {
                    header(HttpHeaders.Cookie, rawToken)
                    setBody(
                        MultiPartFormDataContent(
                            formData {
                                append(
                                    key = "profilePic",
                                    value = file.readBytes(),
                                    headers = Headers.Companion.build {
                                        append(HttpHeaders.ContentType, "image/jpg")
                                        append(
                                            HttpHeaders.ContentDisposition,
                                            "filename=${file.name}.jpg"
                                        )
                                    }
                                )
                            }
                        )
                    )
                }
                Log.d("updateProfile", "Profile updated successfully")
                response
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
    }
}