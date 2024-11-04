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

    // Repository for accessing datastore ( a replacement for shared preferences )
    private val preferenceRepo = PreferencesRepository(context = context)

    // Destination to navigate to after the countdown and checks
    lateinit var navigationDestination: String
        private set

    // StateFlow to notify when the ViewModel is ready to navigate
    val isReadyToNavigate = MutableStateFlow(false)


    init {
        viewModelScope.launch{
            launch{ startCountdown() }
        }
    }


    /**
     * Saves the onboarding completion status in preferences.
     * This indicates that the user has completed the onboarding process.
     */
    suspend fun saveIsOnboarded() {
        preferenceRepo.saveBooleanPreference(ONBOARDING_COMPLETE_KEY, true)
    }


    /**
     * Determines the navigation destination based on the onboarding
     * status and the authentication token.
     *
     * This function sets the `navigationDestination` to the appropriate
     * screen based on whether the onboarding is complete and whether
     * the user is authenticated.
     */
    private suspend fun getNavigationDestination() {

        try {
            val isOnboardingComplete = preferenceRepo.getBooleanPreference(ONBOARDING_COMPLETE_KEY)
            val authToken = preferenceRepo.getStringPreference(AUTH_TOKEN_KEY)

            navigationDestination = when {
                !isOnboardingComplete -> Screens.Onboarding.name
                authToken.isNullOrEmpty() -> Screens.LogIn.name
                else -> Screens.Home.name
            }

        }catch (e: Exception){
            /** TODO for ERIC : navigationDestination should be set to error screen HERE*/
            navigationDestination = Screens.SubmitSignUp.name // temp

        } finally {
            isReadyToNavigate.value = true
            d("FINAL", "IS READY: ${isReadyToNavigate.value}")
        }

    }


    /**
     * Starts a countdown timer that lasts for 4000 milliseconds (4 seconds).
     * When the countdown finishes, it triggers the retrieval of the
     * navigation destination.
     */
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