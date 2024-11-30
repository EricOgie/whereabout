package com.tees.s3186984.whereabout.viewmodel


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tees.s3186984.whereabout.navigation.Screens
import com.tees.s3186984.whereabout.repository.LocalStoreRepository
import com.tees.s3186984.whereabout.wutils.ONBOARDING_COMPLETE_KEY
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import android.os.CountDownTimer
import com.tees.s3186984.whereabout.repository.FirebaseAuthRepository


class LaunchViewModel(context: Context) : ViewModel() {

    private var isCountdownCompleted = false

    // Repository for accessing datastore ( a replacement for shared preferences )
    private val preferenceRepo = LocalStoreRepository(context = context)

    private val firebaseRepo = FirebaseAuthRepository(context, viewModelScope)

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
            val currentUser = firebaseRepo.getCurrentUser()


            navigationDestination = when {
                !isOnboardingComplete -> Screens.Onboarding.name
                currentUser == null -> Screens.LogIn.name
                else -> Screens.Home.name
            }

        }catch (e: Exception){
            navigationDestination = Screens.Error.name

        } finally {
            // Set isReadyToNavigate to true
            isReadyToNavigate.value = true

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