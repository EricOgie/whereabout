package com.tees.s3186984.whereabout.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tees.s3186984.whereabout.repository.FirebaseAuthRepository
import com.tees.s3186984.whereabout.wutils.Helpers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LogInViewModel(context: Context) : ViewModel(){

    // Mutable data state to help control view on login screens
    var emailState =  mutableStateOf("")
    var passwordState = mutableStateOf("")
    var logInRequestMessage =  mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var displayFormError = mutableStateOf(false)
    var displayLogInRequestError = mutableStateOf(false)


    // MutableStateFlow to represent the result of the login operation
    private val _result: MutableStateFlow<Pair<Boolean, String?>> = MutableStateFlow(Pair(false, null))

    // Publicly exposed StateFlow for observing login results
    val result: StateFlow<Pair<Boolean, String?>> get() = _result

    // Repository for handling Firebase authentication
    private val authRepository = FirebaseAuthRepository(context, viewModelScope)


    /**
     * Validates the email and password input fields.
     *
     * @return true if both email and password are valid, false otherwise.
     * This also updates the displayFormError state to show or hide form error messages.
     */
    fun isValidEmailPassword(): Boolean {

        var isValid = Helpers.isValidEmail(emailState.value)
                && Helpers.isValidPassword(passwordState.value)

        displayFormError.value = !isValid

        return  isValid
    }


    /**
     * Initiates the login process.
     * This function sets the loading state to true, calls the authentication repository
     * to perform the login operation, and updates the result and loading states based
     * on the outcome of the login attempt.
     */
    fun logIn() {
        isLoading.value = true
        authRepository.logIn(emailState.value, passwordState.value){ success, message ->
            _result.value = Pair(success, message)
            logInRequestMessage.value = message?: ""
            isLoading.value = false
        }
    }
}
