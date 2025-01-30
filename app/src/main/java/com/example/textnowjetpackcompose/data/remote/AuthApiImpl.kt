package com.example.textnowjetpackcompose.data.remote

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.textnowjetpackcompose.data.HttpRoutes
import com.example.textnowjetpackcompose.data.model.LoginRequest
import com.example.textnowjetpackcompose.data.model.SignupRequest
import com.example.textnowjetpackcompose.data.model.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
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
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

class AuthApiImpl(
    private val client: HttpClient,
    private val dataStore: DataStore<Preferences>
) : AuthApi {

    @Serializable
    data class ErrorResponse(val message: String)

    private val authTokenKey = stringPreferencesKey("auth_token")


    override suspend fun signup(request: SignupRequest): UserResponse {

        try {
            val response: HttpResponse = client.post(HttpRoutes.signup) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status == HttpStatusCode.BadRequest) {
                val errorBody = response.body<String>()
                val errorResponse = Json
                    .decodeFromString<ErrorResponse>(errorBody)
                Log.e(
                    "Cookie Signup",
                    "signup: Server error: ${response.status}, message: ${errorResponse.message}"
                )
                throw Exception("Signup failed: ${errorResponse.message}")
            }

            if (response.status != HttpStatusCode.OK) {
                val errorBody = response.body<String>()
                Log.e("Cookie Signup", "signup: Server error: ${response.status}, body: $errorBody")
                throw Exception("Signup failed with status ${response.status}: $errorBody")
            }

            val token = response.headers[HttpHeaders.SetCookie]
            Log.d("Cookie Signup", "signup: Set-Cookie header: $token")
            if (token != null) {
                dataStore.edit { settings ->
                    settings[authTokenKey] = token
                    Log.d("Cookie Signup", "signup: Cookie stored in DataStore")
                }
            } else {
                Log.e("AuthApiImpl", "signup: Token is null in Set-Cookie header")
                throw Exception("Signup failed: Token is null in Set-Cookie header")
            }


            return response.body()
        } catch (e: Exception) {
            Log.e("AuthApiImpl", "signup: Error during signup", e)
            throw e
        }
    }

    override suspend fun login(request: LoginRequest): UserResponse {
        try {
            val response: HttpResponse = client.post(HttpRoutes.login) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status == HttpStatusCode.BadRequest) {
                val errorBody = response.body<String>()
                val errorResponse = Json.decodeFromString<ErrorResponse>(errorBody)
                Log.e(
                    "Cookie Login",
                    "login: Server error: ${response.status}, message: ${errorResponse.message}"
                )
                throw Exception("Login failed: ${errorResponse.message}")
            }
            if (response.status != HttpStatusCode.OK) {
                val errorBody = response.body<String>()
                Log.e("Cookie Login", "login: Server error: ${response.status}, body: $errorBody")
                throw Exception("Login failed with status ${response.status}: $errorBody")
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
                throw Exception("Login failed: Token is null in Set-Cookie header")
            }
            return response.body()
        } catch (e: Exception) {
            Log.e("Cookie Login", "login: Error during ", e)
            throw e
        }
    }

    private val TAG = "CheckAuth AuthApiImpl"
    override suspend fun checkAuth(): UserResponse {
        val token = dataStore.data.firstOrNull()?.get(authTokenKey)
        Log.d(TAG, "checkAuth: Token from dataStore: $token")
        val rawToken = token?.substringBefore(";")
        Log.d(TAG, "checkAuth: Raw Cookie from DataStore: $rawToken")

        try {
            val response: HttpResponse = client.get(HttpRoutes.checkAuth) {
                headers {
                    append(HttpHeaders.Cookie, "$rawToken")
                }
            }

            Log.d(TAG, "checkAuth: Response status: ${response.status}")
            if (response.status == HttpStatusCode.OK) {
                return response.body()
            } else {
                val errorBody = response.body<String>()
                Log.e(TAG, "checkAuth: HTTP error: ${response.status}, body: $errorBody")
                throw Exception("HTTP error: ${response.status}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "checkAuth: Exception during request", e)
            throw e
        }
    }

    override suspend fun logout(): HttpResponse {
        try {
            val response: HttpResponse = client.post(HttpRoutes.logout)
            Log.d("Logout AuthApiImpl", "logout: Response status: ${response.status}")
            if (!response.status.isSuccess()) {
                val errorBody = response.body<String>()
                Log.e(
                    "Logout AuthApiImpl",
                    "logout: HTTP error: ${response.status}, body: $errorBody"
                )
                throw Exception("HTTP error: ${response.status}")
            }
            dataStore.updateData { settings ->
                settings.toMutablePreferences().apply { remove(authTokenKey) }
            }
            return response
        } catch (e: Exception) {
            Log.e("Logout AuthApiImpl", "logout: Error during logout", e)
            throw e
        }
    }


    override suspend fun updateProfile(bytes: ByteArray): HttpResponse {
        val token = dataStore.data.firstOrNull()?.get(authTokenKey)
        val rawToken = token?.substringBefore(";")

        try {
            val file = File.createTempFile("profile", ".jpg")
            file.writeBytes(bytes)
            Log.d("bro", "updateProfile: ${file.totalSpace}")

            val response: HttpResponse = client.put("https://textnowbackend.onrender.com/api/auth/update-profile") {
                header(HttpHeaders.Cookie, rawToken)
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append(
                                key = "profilePic",
                                value = file.readBytes(),
                                headers = Headers.build {
                                    append(HttpHeaders.ContentType, "image/jpg")
                                    append(HttpHeaders.ContentDisposition, "filename=${file.name}.jpg")
                                }
                            )
                        }
                    )
                )

            }


            println(response.bodyAsText())

            if (!response.status.isSuccess()) {
                val errorBody = response.body<String>()
                Log.e("updateProfile", "HTTP error: ${response.status}, body: $errorBody")
                throw Exception("Update profile failed: ${response.status}, body: $errorBody")
            }

            Log.d("updateProfile", "Profile updated successfully")
            return response
        } catch (e: Exception) {
            Log.e("updateProfile", "Exception during profile update", e)
            throw e
        }
    }


}
