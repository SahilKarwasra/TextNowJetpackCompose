package com.example.textnowjetpackcompose.screens.auth.components

object ValidateSignupForm {
    fun validateUsername(username: String): ValidationResult {
        if (username.trim().isBlank()) {
            return ValidationResult.Invalid("Username cannot be empty")
        }
        if (username.trim().length < 3) {
            return ValidationResult.Invalid("Username must be at least 3 characters long")
        }
        return ValidationResult.Valid
    }

    fun validateEmail(email: String): ValidationResult {
        if (email.trim().isBlank()) {
            return ValidationResult.Invalid("Email cannot be empty")
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult.Invalid("Invalid email format")
        }
        return ValidationResult.Valid
    }

    fun validatePassword(password: String): ValidationResult {
        if (password.trim().isBlank()) {
            return ValidationResult.Invalid("Password cannot be empty")
        }
        if (password.trim().length < 6) {
            return ValidationResult.Invalid("Password must be at least 6 characters long")
        }
        return ValidationResult.Valid
    }
}

sealed class ValidationResult {
    object Valid : ValidationResult()
    data class Invalid(val message: String) : ValidationResult()
}