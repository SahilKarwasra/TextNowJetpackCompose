package com.example.textnowjetpackcompose.config

object HttpRoutes {
    const val BASE_URL = "http://192.168.1.65:5001"

    // Auth Routes
    const val signup = "$BASE_URL/api/auth/signup"
    const val login = "$BASE_URL/api/auth/login"
    const val checkAuth = "$BASE_URL/api/auth/check"
    const val logout = "$BASE_URL/api/auth/logout"
    const val updateProfile = "$BASE_URL/api/auth/update-profile"

//    Messages Routes
    const val getUsers = "$BASE_URL/api/messages/users"
    const val getMessages = "$BASE_URL/api/messages/"
    const val sendMessages = "$BASE_URL/api/messages/send/"
    const val generateText = "$BASE_URL/api/messages/generate-text"
    const val saveFcmToken = "$BASE_URL/api/messages/save-fcm-token"



}