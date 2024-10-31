package com.tees.s3186984.whereabout.navigation

enum class Screens {
    Home,
    SignUp,
    LogIn,
    PasswordResetRequest,
    PasswordReset,
    Profile;


    companion object {
        fun route(path: String?): Screens = when(path?.substringBefore("/")){
            Home.name -> Home
            SignUp.name -> SignUp
            LogIn.name -> LogIn
            PasswordResetRequest.name -> PasswordResetRequest
            PasswordReset.name -> PasswordReset
            Profile.name -> Profile
            // ---- More screens to come in here as the application grows


            // Edge cases
            null -> LogIn
            else -> throw IllegalArgumentException("$path not found")
        }
    }

}