package com.example.textnowjetpackcompose.config

import android.content.Context
import com.example.textnowjetpackcompose.features.home.domain.model.UserResponse
import androidx.core.content.edit

class PreferenceManager(context: Context) {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    companion object{
        const val USER_ID = "id"
        const val USER_NAME = "fullName"
        const val USER_EMAIL= "email"
        const val USER_PROFILE_PIC = "profilePic"
    }

    fun saveUser(user: UserResponse) {
        sharedPreferences.edit { putString(USER_ID, user.id) }
        sharedPreferences.edit { putString(USER_NAME, user.fullName) }
        sharedPreferences.edit { putString(USER_EMAIL, user.email) }
        sharedPreferences.edit { putString(USER_PROFILE_PIC, user.profilePic) }
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

    fun deleteUser() {
        sharedPreferences.edit { putString(USER_ID, null) }
        sharedPreferences.edit { putString(USER_NAME, null) }
        sharedPreferences.edit { putString(USER_EMAIL, null) }
        sharedPreferences.edit { putString(USER_PROFILE_PIC, null) }
    }
}