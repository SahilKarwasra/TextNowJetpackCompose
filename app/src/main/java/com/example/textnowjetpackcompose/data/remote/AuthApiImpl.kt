package com.example.textnowjetpackcompose.data.remote

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.textnowjetpackcompose.data.HttpRoutes
import com.example.textnowjetpackcompose.data.model.SignupRequest
import com.example.textnowjetpackcompose.data.model.AuthUserResponse
import com.example.textnowjetpackcompose.data.model.LoginRequest
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.headers
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable

class AuthApiImpl(
    private val client: HttpClient,
    private val dataStore: DataStore<Preferences>
) : AuthApi {

    @Serializable
    data class ErrorResponse(val message: String)

    private val authTokenKey = stringPreferencesKey("auth_token")

    override suspend fun signup(request: SignupRequest): AuthUserResponse {

        try {
            val response: HttpResponse = client.post(HttpRoutes.signup) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status == HttpStatusCode.BadRequest){
                val errorBody = response.body<String>()
                val errorResponse = Json
                    .decodeFromString<ErrorResponse>(errorBody)
                Log.e("Cookie Signup", "signup: Server error: ${response.status}, message: ${errorResponse.message}")
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
        } catch (e:Exception) {
            Log.e("AuthApiImpl", "signup: Error during signup", e)
            throw e
        }
    }


    override suspend fun login(request: LoginRequest): AuthUserResponse {
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
                Log.e("AuthApiImpl", "login: Token is null in Set-Cookie header")
                throw Exception("Login failed: Token is null in Set-Cookie header")
            }
            return response.body()
        } catch (e: Exception) {
            Log.e("AuthApiImpl", "login: Error during ", e)
            throw e // Re-throw the exception to be handled by the ViewModel
        }
    }

    override suspend fun checkAuth(): AuthUserResponse {
        Log.d("CheckAuth", "checkAuth: Starting checkAuth request")
        val token = dataStore.data.first()[authTokenKey]
        Log.d("CheckAuth", "checkAuth: Cookie From DataStore: $token")

        val response : HttpResponse = client.get(HttpRoutes.checkAuth)
        if (token != null) {
            headers {
                append(HttpHeaders.Cookie, token)
                Log.d("CheckAuth", "checkAuth: Cookie Header sent: $token")
            }
        }

        return when (response.status) {
            HttpStatusCode.OK -> {
                try {
                    val responseBody = response.body<String>()
                    Json.decodeFromString(responseBody)
                } catch (e: Exception) {
                    Log.e("CheckAuth", "checkAuth: Error parsing JSON response", e)
                    throw Exception("Error parsing JSON response")
                }
            }
            else -> {
                Log.e("CheckAuth", "checkAuth: HTTP error: ${response.status}")
                throw Exception("HTTP error: ${response.status}")
            }
        }
    }


}
