package com.tees.s3186984.whereabout.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.tees.s3186984.whereabout.core.DeviceManager
import com.tees.s3186984.whereabout.model.PasswordRecovery
import com.tees.s3186984.whereabout.model.RegistrationFormData
import com.tees.s3186984.whereabout.model.User
import com.tees.s3186984.whereabout.wutils.DEFAULT_ERROR_MSG
import com.tees.s3186984.whereabout.wutils.GENERIC_ERR_MSG
import com.tees.s3186984.whereabout.wutils.NAME
import com.tees.s3186984.whereabout.wutils.PASSWORD_SECURITY
import com.tees.s3186984.whereabout.wutils.USER
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class FirebaseAuthRepository(context: Context, private val scope: CoroutineScope) {

    // Firebase instances
    private val  auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val localStoreRepo = LocalStoreRepository(context)



    /**
     * Logs in a user with their email and password.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @param handleResult A callback function that handles the result of the login attempt,
     *                     receiving a success flag and an optional error message.
     */
    fun logIn(email: String, password: String, handleResult: (Boolean, String?) -> Unit ){

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{task ->
                if (task.isSuccessful) {
                    handleResult(true, null)
                // TODO - check that user still using device registered when they signed up else
                // update user device here
                }else{
                    var errorMessage  = task.exception?.message ?: DEFAULT_ERROR_MSG
                    handleResult(false, errorMessage)
                }
            }

    }


    /**
     * Signs up a new user with the provided registration form data.
     *
     * @param formData The data required for user registration.
     * @param handleResult A callback function that handles the result of the signup attempt,
     *                     receiving a success flag and an optional error message.
     */
    fun signUp(formData: RegistrationFormData, handleResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(formData.email, formData.password)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful){
                    handleResult(false, task.exception?.message)
                    return@addOnCompleteListener
                }

                if (task.isSuccessful){
                    val userId = auth.currentUser?.uid

                    if(userId.isNullOrBlank()){
                        handleResult(false, GENERIC_ERR_MSG)
                        return@addOnCompleteListener
                    }

                    // I will pass null to makeDevice so it can create a fresh deviceId since
                    // the user is just setting up
                    val userDevice = DeviceManager.makeDevice(deviceId=null)
                    val user = formData.makeUser(userId, userDevice)
                    val recoveryData = PasswordRecovery(
                        question = formData.recoveryQuestion,
                        answer = formData.recoveryAnswer
                    )

                    val firestoreBatch = firestore.batch()

                    // Stage user data
                    val userReference = firestore.collection(USER).document(userId)
                    firestoreBatch.set(userReference, user)

                    // Stage password recovery question and answer
                    val passwordRecoveryReference = firestore.collection(PASSWORD_SECURITY).document(userId)
                    firestoreBatch.set(passwordRecoveryReference, recoveryData)

                    // Commit set collections in firestore
                    firestoreBatch.commit().addOnCompleteListener { fireStoreTask ->
                        if (fireStoreTask.isSuccessful){
                            handleResult(true, null)
                            // Save device ID to local store using user ID as key
                            scope.launch{
                                localStoreRepo.saveStringPreference(userId, userDevice.deviceId)
                                localStoreRepo.saveStringPreference(NAME, user.name)
                            }
                        } else{
                            handleResult(false, fireStoreTask.exception?.message)
                        }
                    }

                }

            }

    }


    /**
     * Retrieves the currently authenticated user.
     *
     * @return The current FirebaseUser, or null if no user is logged in.
     */
    fun getCurrentUser() : FirebaseUser? {
        return auth.currentUser
    }


    /**
     * Signs out the currently authenticated user.
     */
    fun signOut() {
        auth.signOut()
    }

}