package com.tees.s3186984.whereabout.wutils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import com.tees.s3186984.whereabout.ui.theme.WMeRed

const val SIGNUP_GREETING = "Let's get you started real quick. We only need a few information"

const val SIGNUP_NUDGE = "One more step and you are done"

const val PASSWORD_RECOVERY_INFO = "If you need to recover your password, you will be required to provide " +
        "the Password Recovery Answer above"

const val ONBOARDING_MSG = "Stay safe and connected with real-time location tracking. Whether you're" +
        " exploring or in an emergency, we're here to help. Let’s get started!"

const val DEFAULT_ERROR_MSG = "Something went wrong"

const val EMAIL_ERROR_MSG = "* Email must be a valid email address"
const val PASSWORD_ERROR_MSG =  "* Password must 8 or more characters and contain special char"
const val NAME_ERROR_MSG = "* Fullname must be 3 - 20 letters long"
const val PHRASE_ERROR_MSG = "* question and answer must be 5 - 20 letters long"
const val GENERIC_ERR_MSG = "Unknown Error"

// ---------------------- ANNOTATED CONSTANTS ---------------------------- //
val AGREE_MESSAGE =  buildAnnotatedString {
    append("By Clicking Agree and Continue below, I agree to Whereabout ")
    pushStyle(SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline))
    append("Terms of Service and Privacy Policy")
    pop()
}

var APP_NAME_COLORED = buildAnnotatedString {
    append("wher")
    pushStyle(SpanStyle(color = WMeRed))
    append("ea")
    pop()
    append("bout")
}

var WELCOME = buildAnnotatedString {
    append("Welcome to Wher")
    pushStyle(SpanStyle(color = WMeRed))
    append("ea")
    pop()
    append("bout")
}


//----------------------- TITLES RELATED CONSTANTS ----------------------- //
const val NAME = "fullname"
const val REGISTER = "Sign Up"
const val LOGIN = "Sign In"
const val EMAIL = "Email"
const val PASSWORD = "Password"
const val AGREE = "Agree and Continue"
const val  SECURITY_QUESTION = "Password Recovery Question"
const val ANSWER = "Password Recovery Answer"
const val SIGNUP = "register"
const val LOCAL_STORE  = "local_store"

//----------------------- FIRESTORE RELATED CONSTANTS ----------------------- //
const val PASSWORD_SECURITY = "password_security"
const val USER = "user"


const val ONBOARDING_COMPLETE_KEY = "onboarding_complete"
const val AUTH_TOKEN_KEY = "auth_token"
const val FORGET_PASS = "Forget your password?"
const val ALREADY_HAVE = "Already have an account?"
const val DONT_HAVE = "Don't have an account?"

