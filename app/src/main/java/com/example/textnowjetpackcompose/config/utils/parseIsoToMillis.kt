package com.example.textnowjetpackcompose.config.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun parseIsoToMillis(iso: String): Long {
    return try {
        SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.getDefault()
        ).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.parse(iso)?.time ?: System.currentTimeMillis()
    } catch (e: Exception) {
        System.currentTimeMillis()
    }
}
