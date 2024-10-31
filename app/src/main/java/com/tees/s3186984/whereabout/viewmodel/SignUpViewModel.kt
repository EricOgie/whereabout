package com.tees.s3186984.whereabout.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tees.s3186984.whereabout.model.RegistrationFormData
import com.tees.s3186984.whereabout.repository.FirebaseAuthRepository
import com.tees.s3186984.whereabout.wutils.Helpers

class SignUpViewModel : ViewModel() {

    // States for form input fields
    var fullNameState = mutableStateOf("") // Holds the user's full name
    var emailState = mutableStateOf("") // Holds the user's email address
    var passwordState = mutableStateOf("") // Holds the user's password
    var questionState = mutableStateOf("") // Holds the recovery question
    var answerState = mutableStateOf("") // Holds the answer to the recovery question
    var sinUpRequestMessage = mutableStateOf("") // Holds the firebase returned signup message
    var loadingState = mutableStateOf(false) // Indicates whether the sign-up process is in progress
    var displaySignUpRequestError = mutableStateOf(false)
    var displayScreen1ErrorState = mutableStateOf(false) // Indicates validation errors for signUp screen 1 (name, email, password)
    var displayScreen2ErrorState = mutableStateOf(false) // Indicates validation errors for signUp screen 2 (recovery question/answer)

    // Repository for Firebase authentication operations
    private val authRepo = FirebaseAuthRepository()

    // LiveData to observe the result of the sign-up process
    private val _result = MutableLiveData<Pair<Boolean, String?>>()
    val result: LiveData<Pair<Boolean, String?>> get() = _result


    /**
     * Validates the full name, email, and password.
     * Updates the displayScreen1ErrorState based on validation result.
     * @return true if all inputs are valid; false otherwise.
     */
    fun isValidNameEmailPassword(): Boolean {
        var isValid = Helpers.isValidFullName(fullNameState.value)
                && Helpers.isValidEmail(emailState.value)
                && Helpers.isValidPassword(passwordState.value)

        displayScreen1ErrorState.value = !isValid
        return isValid
    }


    /**
     * Validates all form data including the recovery question and answer.
     * Updates the displayScreen2ErrorState based on validation result.
     * @return true if all form data is valid; false otherwise.
     */
    fun isValidFormData(): Boolean {
        var isValid = isValidNameEmailPassword()
                && Helpers.isValidPhrase(questionState.value)
                && Helpers.isValidPhrase(answerState.value)

        displayScreen2ErrorState.value = !isValid
        return isValid
    }

    /**
     * Initiates the sign-up process by creating a RegistrationFormData object
     * and invoking the sign-up method in the authentication repository.
     * Updates the loading state and result based on the outcome.
     */
    fun signUp() {

        val userData = RegistrationFormData(
            name = fullNameState.value,
            email = emailState.value,
            password = passwordState.value,
            recoveryQuestion = questionState.value,
            recoveryAnswer = answerState.value
        )

        loadingState .value = true
        authRepo.signUp(formData = userData){ success, message ->
            _result.value = Pair(success, message)
            sinUpRequestMessage.value = message ?: ""
            loadingState .value = false
        }

    }

}