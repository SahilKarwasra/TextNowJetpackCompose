package com.example.textnowjetpackcompose.data

object HttpRoutes {
    private const val BASE_URL = "https://textnowbackend.onrender.com"
    const val signup = "$BASE_URL/api/auth/signup"
    const val login = "$BASE_URL/api/auth/login"
    const val checkAuth = "$BASE_URL/api/auth/check"
    const val logout = "$BASE_URL/api/auth/logout"
    const val getUsers = "$BASE_URL/api/messages/users"
    const val getMessages = "$BASE_URL/api/messages/"
    const val sendMessages = "$BASE_URL/api/messages/send/"
}