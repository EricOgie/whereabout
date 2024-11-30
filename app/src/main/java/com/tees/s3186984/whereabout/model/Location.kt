package com.tees.s3186984.whereabout.model


data class Location (
    val id: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)


data class ShareLocationBucket (
    val bucketId: String,
    val ownerId: String,
    val users: List<String>,
    val expireAt: Long
)