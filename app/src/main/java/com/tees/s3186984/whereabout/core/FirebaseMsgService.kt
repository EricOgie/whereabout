package com.tees.s3186984.whereabout.core

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.tees.s3186984.whereabout.model.ConnectionRequest
import com.tees.s3186984.whereabout.model.Notification
import com.tees.s3186984.whereabout.repository.LocalStoreRepository
import com.tees.s3186984.whereabout.wutils.FCM_TOKEN_KEY
import kotlinx.coroutines.*


class FirebaseMsgService : FirebaseMessagingService() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val gson = Gson()

    override fun onNewToken(token: String) {
        // when a new Firebase cloud messaging token is generated, we save
       //in local store for later use for push notification
        saveTokenLocally(token)
    }

    private fun saveTokenLocally(token: String) {
        val localStoreRepo = LocalStoreRepository(applicationContext)
        applicationScope.launch {
            try {
                localStoreRepo.saveStringPreference(FCM_TOKEN_KEY, token)
            } catch (e: Exception) {
                Log.e("FirebaseMsgService", "Error saving token to DataStore", e)
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val notificationMessage = remoteMessage.notification?.body ?: "No message"
        // Convert notification string to Notification object
        val notification  = gson.fromJson(notificationMessage, Notification::class.java)
        // Add notification to a global notification list
        NotificationManager.addNotification(notification)
    }

}