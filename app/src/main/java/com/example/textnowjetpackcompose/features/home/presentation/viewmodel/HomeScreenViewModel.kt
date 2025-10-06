package com.example.textnowjetpackcompose.features.home.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.textnowjetpackcompose.config.PreferenceManager
import com.example.textnowjetpackcompose.features.home.domain.repo.HomeRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class HomeScreenViewModel(
    private val homeRepo: HomeRepo,
    applicationContext: Context
) : ViewModel() {

    private val _state = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
    val state = _state.asStateFlow()

    init {
        getUsers()
    }
    val preferenceManager = PreferenceManager(context = applicationContext)
    val currentUser = mutableStateOf(preferenceManager.getUser())


    fun getUsers() {
        viewModelScope.launch {
            _state.value = HomeScreenState.Loading
            try {
                val result = homeRepo.getAllUser()
                result.fold(onSuccess = {
                    if (it.isEmpty()) {
                        _state.value = HomeScreenState.Empty
                    } else {
                        val users = it.map { user ->
                            user.copy(
                                lastMessage = user.lastMessage?.let { msg ->
                                    msg.copy(
                                        createdAt = formatTimestamp(msg.createdAt)
                                    )
                                })

                        }
                        _state.value = HomeScreenState.Success(users)
                    }

                }, onFailure = {
                    _state.value = HomeScreenState.Error(it.message.toString())
                })
            } catch (e: Exception) {
                _state.value = HomeScreenState.Error(e.message.toString())
            }
        }
    }

    suspend fun formatTimestamp(isoTimestamp: String?): String {
        if (isoTimestamp.isNullOrBlank()) {
            return ""
        }

        return withContext(Dispatchers.Default) {
            try {
                val instant = Instant.parse(isoTimestamp)
                val messageTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
                val now = ZonedDateTime.now(ZoneId.systemDefault())

                val minutesAgo = ChronoUnit.MINUTES.between(messageTime, now)
                val hoursAgo = ChronoUnit.HOURS.between(messageTime, now)
                val daysAgo = ChronoUnit.DAYS.between(messageTime.toLocalDate(), now.toLocalDate())

                when {
                    minutesAgo < 1 -> "Just now"
                    minutesAgo < 60 -> "${minutesAgo}m"
                    hoursAgo < 24 -> "${hoursAgo}h"
                    daysAgo == 1L -> "Yesterday"
                    daysAgo < 7 -> messageTime.format(DateTimeFormatter.ofPattern("EEE"))
                    daysAgo < 365 -> messageTime.format(DateTimeFormatter.ofPattern("MMM d"))
                    else -> messageTime.format(DateTimeFormatter.ofPattern("M/d/yy"))
                }
            } catch (e: Exception) {
                Log.e("HomeScreenViewModel", "Error parsing timestamp: $isoTimestamp", e)
                ""
            }
        }
    }
}

