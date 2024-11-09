package com.tees.s3186984.whereabout.core

import android.os.Build
import com.google.gson.Gson
import com.tees.s3186984.whereabout.model.ConnectionRequest
import com.tees.s3186984.whereabout.model.Device
import java.security.MessageDigest
import java.util.UUID
import kotlin.String

object DeviceManager {

    private val brand = Build.BRAND
    private val model = Build.MODEL
    private val osVersion = Build.VERSION.RELEASE
    private val deviceName = "${Build.MANUFACTURER} ${Build.MODEL}"

    val gson = Gson()


    fun makeDevice(deviceId: String?): Device{
        return Device(
            deviceId = deviceId?: generateDeviceUUID(),
            model = model,
            brand = brand,
            deviceName = deviceName,
        )
    }

    fun makeConnectionRequestData(userId: String, userName: String, deviceId: String): String{
        val initiatorFirstName = userName.split(" ").firstOrNull() ?: ""

        return gson.toJson(ConnectionRequest(
            initiatorId = userId,
            initiatorFirstName = initiatorFirstName,
            initiatorDevice = makeDevice(deviceId),
            createdAt = System.currentTimeMillis()
        ))
    }

    fun makeConnectionRequest(jsonData: String): ConnectionRequest {
        return gson.fromJson(jsonData, ConnectionRequest::class.java)
    }


    private fun generateDeviceUUID(): String {
        val uuid = UUID.randomUUID().toString().take(8)
        // Combine device properties with a random suffix
        val combinedString = "${osVersion}_${model}_${brand}_${uuid}"
        // Hash the combined string using SHA-256
        val digest = MessageDigest.getInstance("SHA-256").digest(combinedString.toByteArray())
        // Convert the byteArray, digest to a hexadecimal string
        val hexString = digest.joinToString("") { "%02x".format(it) }

        return buildString {
            append(hexString.substring(0, 8))
            append("-")
            append(hexString.substring(8, 12))
            append("-")
            append(hexString.substring(12, 16))
            append("-")
            append(hexString.substring(16, 20))
            append("-")
            append(hexString.substring(20, 32))
        }
    }

}
