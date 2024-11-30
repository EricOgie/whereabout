package com.tees.s3186984.whereabout.viewmodel

import androidx.lifecycle.ViewModel
import com.tees.s3186984.whereabout.core.NotificationManager
import com.tees.s3186984.whereabout.model.Notification
import kotlinx.coroutines.flow.StateFlow

/**
 * This class sole existence is to expose the globally accessible
 * state list of notifications
 * */
class NotificationViewmodel: ViewModel(){
    // Expose the notifications from NotificationManager as a StateFlow
    val notifications: StateFlow<List<Notification>> = NotificationManager.notifications
}