package com.tees.s3186984.whereabout.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tees.s3186984.whereabout.model.User
import com.tees.s3186984.whereabout.repository.LocalStoreRepository
import com.tees.s3186984.whereabout.wutils.FETCHING_ERROR
import com.tees.s3186984.whereabout.wutils.PossibleRequestState
import com.tees.s3186984.whereabout.wutils.USER
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel(context: Context) : ViewModel(){

    val gson = Gson()
    // Repositories for Local Storage
    val localStoreRepo = LocalStoreRepository(context = context)

    private val _requestResultState = MutableStateFlow<PossibleRequestState<User>>(PossibleRequestState.Idle)
    val requestResultState: StateFlow<PossibleRequestState<User>> get() = _requestResultState


    // Initialize the ViewModel by fetching the cached user and paired connections
    init {
        viewModelScope.launch{
            getCachedUser()
        }
    }

    /**
     * Retrieves the cached user information from local storage and
     *      sets it to the value of  _userDataState.
     *
     * Uses Gson for JSON deserialization.
     */
    private suspend fun getCachedUser(){
        _requestResultState.value = PossibleRequestState.ScreenLoading
        try {
            val userAsJson = localStoreRepo.getStringPreference(USER)

            if (userAsJson!!.isNotEmpty()){
                val user = gson.fromJson(userAsJson, User::class.java)
                _requestResultState.value = PossibleRequestState.SuccessWithData(user)
            }else {
                _requestResultState.value = PossibleRequestState.Failure(FETCHING_ERROR + USER)
            }

        }catch (e: Exception){

            _requestResultState.value = PossibleRequestState.Failure(FETCHING_ERROR + USER)
            e.printStackTrace()
        }
    }

}