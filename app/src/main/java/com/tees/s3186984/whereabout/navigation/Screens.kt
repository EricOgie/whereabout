package com.tees.s3186984.whereabout.navigation

enum class Screens {
    Home,
    SignUp,
    SubmitSignUp,
    LogIn,
    PasswordResetRequest,
    PasswordReset,
    Splash,
    Onboarding,
    Profile,
    Error,
    Connection;


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

        fun screenWithBottomNavBar(): List<String>{
            return listOf(Home.name, Profile.name, Connection.name)
        }
    }

}