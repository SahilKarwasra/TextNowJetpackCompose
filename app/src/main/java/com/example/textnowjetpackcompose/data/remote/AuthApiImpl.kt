package com.example.textnowjetpackcompose.data.remote

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.textnowjetpackcompose.data.HttpRoutes
import com.example.textnowjetpackcompose.data.model.SignupRequest
import com.example.textnowjetpackcompose.data.model.AuthUserResponse
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

class AuthApiImpl(private val client: HttpClient,private val dataStore: DataStore<Preferences>) : AuthApi {

    private val authTokenKey = stringPreferencesKey("auth_token")

    override suspend fun signup(request: SignupRequest): AuthUserResponse {

        val response : HttpResponse = client.post(HttpRoutes.signup) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        val token = response.headers[HttpHeaders.SetCookie]
        Log.d("Cookie Signup", "signup: Set-Cookie header: $token")
        if (token != null) {
            dataStore.edit { settings ->
                settings[authTokenKey] = token
                Log.d("Cookie Signup", "signup: Cookie stored in DataStore")
            }
        }

        return response.body()
    }


//    var authUser = mutableStateOf(null)
//    val isCheckingAuth = mutableStateOf(true)
//
//    override suspend fun checkAuth(){
//        try {
//            Log.d("Cookie CheckAuth", "checkAuth: Starting checkAuth request")
//            val res = client.get(HttpRoutes.checkAuth)
//            Log.d("Cookie CheckAuth", "checkAuth: Response: ${res.status}")
//            authUser = res.body()
//        } catch (e: Exception) {
//            Log.d("Cookie CheckAuth", "checkAuth: Error during checkAuth")
//            authUser = null
//            e.printStackTrace()
//        } finally {
//            isCheckingAuth.value = false
//        }
//    }



    override suspend fun checkAuth(): AuthUserResponse {
        Log.d("Cookie CheckAuth", "checkAuth From AuthApiImpl: Starting checkAuth request")
        val token = dataStore.data.first()[authTokenKey]
        Log.d("Cookie CheckAuth", "checkAuth From AuthApiImpl: Cookie from DataStore: $token")
        val response: HttpResponse =  client.get(HttpRoutes.checkAuth) {
            if (token != null) {
                headers{
                   append(HttpHeaders.Cookie, token)
                    Log.d("Cookie CheckAuth", "checkAuth From AuthApiImpl: Cookie header sent: $token")
                }
            }
        }
        return when(response.status) {
            HttpStatusCode.OK -> {
                try {
                    val responseBody = response.body<String>()
                    Json.decodeFromString(responseBody)
                } catch (e: Exception) {
                    Log.e("Cookie CheckAuth", "checkAuth: Error parsing JSON response", e)
                    throw Exception("Error parsing JSON response")
                }
            }
            else -> {
                Log.e("Cookie CheckAuth", "checkAuth: HTTP error: ${response.status}")
                throw Exception("HTTP error: ${response.status}")
            }

        }
    }
}
