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
    val userId: String,
    val name: String,
    val email: String,
    val profileImage: String
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
    fun makeUser(id: String) : User {
        return User(
            userId = id,
            name = name,
            email = email,
            profileImage = ""
        )
    }
}


