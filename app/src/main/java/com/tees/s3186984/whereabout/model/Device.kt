package com.tees.s3186984.whereabout.model

data class Device(
    val deviceId: String,         // Unique device identifier
    val tagName: String = "", // User-defined tag name for the device (eg, "Dad's phone")
    val model: String,       // Device model
    val brand: String,
    val deviceName: String,
    val userId: String?,    // Optional: ID of the user associated with this device
    val status: DeviceStatus = DeviceStatus.INACTIVE,    // Current status of the device can be {ACTIVE,INACTIVE,SUSPENDED}
)

enum class DeviceStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED
}