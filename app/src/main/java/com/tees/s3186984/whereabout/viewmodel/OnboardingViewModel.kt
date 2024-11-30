package com.tees.s3186984.whereabout.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tees.s3186984.whereabout.repository.LocalStoreRepository
import com.tees.s3186984.whereabout.wutils.ONBOARDING_COMPLETE_KEY
import kotlinx.coroutines.launch

class OnboardingViewModel(context: Context): ViewModel(){
    // LocalStoreRepository instance to handle storing preferences locally
    private val preferenceRepo = LocalStoreRepository(context = context)

    /**
     * Initializes the ViewModel and triggers the onboarding process
     * by calling the onboardUser method.
     */
    init {
        onboardUser()
    }

    /**
     * Onboards the user by saving a boolean preference indicating that the
     * onboarding process is complete. This is done asynchronously using
     * viewModelScope to ensure it doesn't block the main thread.
     */
    private fun onboardUser(){
        viewModelScope.launch{
            preferenceRepo.saveBooleanPreference(ONBOARDING_COMPLETE_KEY, true)
        }

    }
}