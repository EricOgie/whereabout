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


//----------------------- TITLES RELATED CONSTANTS ----------------------- //
const val NAME = "fullname"
const val REGISTER = "Sign Up"
const val LOGIN = "SignIn"
const val EMAIL = "Email"
const val PASSWORD = "Password"
const val AGREE = "Agree and Continue"
const val SUBMIT_BTN_LABEL = "Register me"
const val  SECURITY_QUESTION = "Password Recovery Question"
const val ANSWER = "Password Recovery Answer"
const val SIGNUP = "register"
const val LOCAL_STORE  = "local_store"

val ONBOARDING_COMPLETE_KEY = "onboarding_complete"
val AUTH_TOKEN_KEY = "auth_token"
