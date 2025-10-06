package com.example.textnowjetpackcompose.features.home.domain.repo

import com.example.textnowjetpackcompose.features.home.domain.model.UserResponse

interface HomeRepo {
    suspend fun getAllUser(): Result<List<UserResponse>>
}