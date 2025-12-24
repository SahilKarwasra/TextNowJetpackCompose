package com.example.textnowjetpackcompose.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.textnowjetpackcompose.config.PreferenceManager
import com.example.textnowjetpackcompose.features.auth.data.remote.AuthApi
import com.example.textnowjetpackcompose.features.auth.data.repository.AuthRepoImpl
import com.example.textnowjetpackcompose.features.auth.domain.repository.AuthRepo
import com.example.textnowjetpackcompose.features.auth.presentation.viewmodel.AuthViewModel
import com.example.textnowjetpackcompose.config.SocketHandler
import com.example.textnowjetpackcompose.features.chat.data.remote.ChatApi
import com.example.textnowjetpackcompose.features.chat.data.repo.ChatRepoImpl
import com.example.textnowjetpackcompose.features.chat.domain.repo.ChatRepo
import com.example.textnowjetpackcompose.features.chat.presentation.viewmodel.ChatViewModel
import com.example.textnowjetpackcompose.features.home.data.remote.HomeApi
import com.example.textnowjetpackcompose.features.home.data.repo.HomeRepoImpl
import com.example.textnowjetpackcompose.features.home.domain.repo.HomeRepo
import com.example.textnowjetpackcompose.features.home.presentation.viewmodel.HomeScreenViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val appModule = module {
    // Ktor Client
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
            install(WebSockets)
        }
    }
    // Utils
    single { get<Context>().dataStore }
    single { SocketHandler }

    // Preference Manager
    singleOf(::PreferenceManager)

    // Auth Dependencies
    singleOf(::AuthApi)
    singleOf(::AuthRepoImpl).bind(AuthRepo::class)
    viewModelOf(::AuthViewModel)


    // Home Dependencies
    singleOf(::HomeApi)
    singleOf(::HomeRepoImpl).bind(HomeRepo::class)
    viewModelOf(::HomeScreenViewModel)

    // Chat Dependencies
    singleOf(::ChatApi)
    singleOf(::ChatRepoImpl).bind(ChatRepo::class)
    viewModelOf(::ChatViewModel)

}