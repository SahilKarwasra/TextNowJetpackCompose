package com.example.textnowjetpackcompose.data

object HttpRoutes {
    private const val BASE_URL = "http://10.0.2.2:5001"
    const val signup = "$BASE_URL/api/auth/signup"
    const val login = "$BASE_URL/api/auth/login"
    const val checkAuth = "$BASE_URL/api/auth/check"
}