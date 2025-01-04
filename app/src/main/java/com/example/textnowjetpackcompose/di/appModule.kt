package com.example.textnowjetpackcompose.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.textnowjetpackcompose.data.remote.AuthApi
import com.example.textnowjetpackcompose.data.remote.AuthApiImpl
import com.example.textnowjetpackcompose.data.remote.MessageApi
import com.example.textnowjetpackcompose.data.remote.MessageApiImpl
import com.example.textnowjetpackcompose.data.repository.AuthRepository
import com.example.textnowjetpackcompose.data.repository.AuthRepositoryImpl
import com.example.textnowjetpackcompose.data.repository.MessageRepository
import com.example.textnowjetpackcompose.data.repository.MessageRepositoryImpl
import com.example.textnowjetpackcompose.viewmodels.AuthViewModel
import com.example.textnowjetpackcompose.viewmodels.ChatViewModels
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val appModule = module {
    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
            }
            install(HttpCookies) {
                storage = AcceptAllCookiesStorage()
            }
        }
    }
    single {
        val client = get<HttpClient>()
        val dataStore = get<DataStore<Preferences>>()
        AuthApiImpl(client, dataStore) as AuthApi
    }
    single {
        val authApi = get<AuthApi>()
        val dataStore = get<DataStore<Preferences>>()
        AuthRepositoryImpl(authApi, dataStore) as AuthRepository
    }
    single { get<Context>().dataStore }
    viewModelOf(::AuthViewModel)
    single {
        val client = get<HttpClient>()
        val dataStore = get<DataStore<Preferences>>()

        MessageApiImpl(client, dataStore) as MessageApi
    }
    single {
        val messageApi = get<MessageApi>()
        MessageRepositoryImpl(messageApi) as MessageRepository
    }
    viewModelOf(::ChatViewModels)

}