package com.example.textnowjetpackcompose.features.home.data.repo

import com.example.textnowjetpackcompose.features.home.data.remote.HomeApi
import com.example.textnowjetpackcompose.features.home.domain.model.UserResponse
import com.example.textnowjetpackcompose.features.home.domain.repo.HomeRepo

class HomeRepoImpl(
    private val homeApi: HomeApi
): HomeRepo {

    override suspend fun getAllUser(): Result<List<UserResponse>> {
        try {
            val result = homeApi.getUsers()
            return Result.success(result)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}