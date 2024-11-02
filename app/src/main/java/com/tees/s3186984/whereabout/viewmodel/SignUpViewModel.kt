package com.tees.s3186984.whereabout.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.tees.s3186984.whereabout.wutils.Helpers

class SignUpViewModel : ViewModel() {

    var fullNameState = mutableStateOf("")
    var emailState =  mutableStateOf("")
    var passwordState = mutableStateOf("")
    var questionState = mutableStateOf("")
    var answerState = mutableStateOf("")

    fun isValidNameEmailPassword(): Boolean {
        return Helpers.isValidFullName(fullNameState.value)
                && Helpers.isValidEmail(emailState.value)
                && Helpers.isValidPassword(passwordState.value)
    }


    fun isValidFormData(): Boolean {
        return isValidNameEmailPassword()
                && Helpers.isValidPhrase(questionState.value)
                && Helpers.isValidPhrase(answerState.value)
    }

    fun signUp() {
        Log.d("VM", "fullname: ${fullNameState.value} email: ${emailState.value} Pword: ${passwordState.value}" +
                " Q: ${questionState.value} A: ${answerState.value}")

    }
}