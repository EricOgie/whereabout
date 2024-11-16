package com.tees.s3186984.whereabout.core

import android.os.Build
import com.tees.s3186984.whereabout.model.Device
import com.tees.s3186984.whereabout.model.DeviceStatus
import java.util.UUID
import kotlin.String

object DeviceManager {

    private val brand = Build.BRAND
    private val model = Build.MODEL
    private val deviceName = "${Build.MANUFACTURER} ${Build.MODEL}"


    fun makeDevice(deviceUserId: String): Device{
        return Device(
            deviceId = UUID.randomUUID().toString(),
            tagName = "",
            model = model,
            brand = brand,
            deviceName = deviceName,
            userId = deviceUserId,
            status = DeviceStatus.INACTIVE
        )
    }

}
