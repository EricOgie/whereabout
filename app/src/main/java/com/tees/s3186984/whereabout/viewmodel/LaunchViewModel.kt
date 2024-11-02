package com.tees.s3186984.whereabout.viewmodel


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tees.s3186984.whereabout.navigation.Screens
import com.tees.s3186984.whereabout.repository.PreferencesRepository
import com.tees.s3186984.whereabout.wutils.AUTH_TOKEN_KEY
import com.tees.s3186984.whereabout.wutils.ONBOARDING_COMPLETE_KEY
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import android.util.Log.d
import android.os.CountDownTimer



class LaunchViewModel(context: Context) : ViewModel() {

    private var isCountdownCompleted = false
    private val preferenceRepo = PreferencesRepository(context = context)

    lateinit var navigationDestination: String
        private set

    val isReadyToNavigate = MutableStateFlow(false)


    init {
        viewModelScope.launch{
            launch{ startCountdown() }
        }
    }


    suspend fun saveIsOnboarded() {
        preferenceRepo.saveBooleanPreference(ONBOARDING_COMPLETE_KEY, true)
    }


    private suspend fun getNavigationDestination() {

        try {
            val isOnboardingComplete = preferenceRepo.getBooleanPreference(ONBOARDING_COMPLETE_KEY)
            val authToken = preferenceRepo.getStringPreference(AUTH_TOKEN_KEY)

            navigationDestination = when {
                !isOnboardingComplete -> Screens.Onboarding.name
                authToken.isNullOrEmpty() -> Screens.LogIn.name
                else -> Screens.Home.name
            }
            d("LaunchViewModel", "DESTINATION: $navigationDestination")
            isReadyToNavigate.value = true
        }catch (e: Exception){
            d("LaunchViewModel", "getNavigationDestination Exception: ${e.message}")
            /** TODO for ERIC : navigationDestination should be set to error screen HERE*/
            navigationDestination = Screens.SubmitSignUp.name // temp
            isReadyToNavigate.value = true
        } finally {
            isReadyToNavigate.value = true
            d("FINAL", "IS READY: ${isReadyToNavigate.value}")
        }

    }


    fun startCountdown() {
        if (isCountdownCompleted) return
        val timer = object : CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                viewModelScope.launch {
                    isCountdownCompleted = true
                    getNavigationDestination()
                }
            }
        }
        timer.start()
    }




}