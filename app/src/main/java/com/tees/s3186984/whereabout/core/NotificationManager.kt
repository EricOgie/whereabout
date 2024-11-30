package com.tees.s3186984.whereabout.core

import com.tees.s3186984.whereabout.model.Notification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object NotificationManager {
    // StateFlow to hold the list of notifications
    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications

    // Method to add a new notification to the list
    fun addNotification(notification: Notification) {
        _notifications.value = _notifications.value + notification
    }
}