package com.tees.s3186984.whereabout.model

import kotlin.String

/**
 * Represents a user in the application.
 *
 * @param userId Unique identifier for the user.
 * @param name The full name of the user.
 * @param email The user's email address, used for authentication and communication.
 * @param profileImage URL or path to the user's profile image.
 */
data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val profileImage: String = "",
    val device: Device = Device()
)

/**
 * Represents a password recovery entry associated with a user.
 *
 * @param question The security question used for password recovery.
 * @param answer The answer to the security question, which the user provides to verify their identity.
 */
data class PasswordRecovery(
    val question: String,
    val answer: String
)

/**
 * Represents a one-time password (OTP) for user verification.
 *
 * @param userId Unique identifier for the user associated with this OTP.
 * @param otpValue The actual OTP value generated for user verification.
 * @param expiresAt The timestamp (in milliseconds) indicating when the OTP will expire.
 */
data class Otp(
    val userId: String,
    val otpValue: String,
    val expiresAt: Long
)


data class RegistrationFormData(
    val name: String,
    val email: String,
    val password: String,
    val recoveryQuestion: String,
    val recoveryAnswer: String
){
    fun makeUser(id: String, userDevice: Device) : User {
        return User(
            userId = id,
            name = name,
            email = email,
            profileImage = "",
            device = userDevice
        )
    }
}

/**
 * Embedding the Device data in models like User and Connection introduces redundancy,
 * since the same device information can exist in other places (e.g.,Connection model). This could lead to potential data inconsistencies.
 *
 * However, this trade-off is accepted in order to avoid multiple queries, as Firebase
 * does not support joins or complex query operations (e.g., querying connections and
 * separately fetching device details from a different collection).
 *
 * By embedding the device information, we ensure that querying the connections
 * is fast and efficient, without the need for additional lookups. This approach
 * favors performance and simplicity in data retrieval, but requires careful management
 * of data consistency.
 */
data class Device(
    val deviceId: String = "",
    val model: String = "",
    val brand: String = "",
    val deviceName: String = "",
)


