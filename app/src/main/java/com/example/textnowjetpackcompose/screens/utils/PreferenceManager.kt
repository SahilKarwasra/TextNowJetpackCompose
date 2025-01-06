package com.example.textnowjetpackcompose.screens.utils

import android.content.Context
import com.example.textnowjetpackcompose.data.model.UserResponse

class PreferenceManager(context: Context) {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    companion object{
        const val USER_ID = "id"
        const val USER_NAME = "fullName"
        const val USER_EMAIL= "email"
        const val USER_PROFILE_PIC = "profilePic"
    }

    fun saveUser(user: UserResponse) {
        sharedPreferences.edit().putString(USER_ID, user.id).apply()
        sharedPreferences.edit().putString(USER_NAME, user.fullName).apply()
        sharedPreferences.edit().putString(USER_EMAIL, user.email).apply()
        sharedPreferences.edit().putString(USER_PROFILE_PIC, user.profilePic).apply()
    }

    fun getUser(): UserResponse? {
        val id = sharedPreferences.getString(USER_ID, null)
        val name = sharedPreferences.getString(USER_NAME, null)
        val email = sharedPreferences.getString(USER_EMAIL, null)
        val profilePic = sharedPreferences.getString(USER_PROFILE_PIC, null)
        return if (id != null && name != null && email != null) {
            UserResponse(id, name, email, profilePic)
        } else {
            null
        }
    }
}