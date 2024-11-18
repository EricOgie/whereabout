package com.tees.s3186984.whereabout.viewmodel

import android.app.Activity
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.gson.Gson
import com.tees.s3186984.whereabout.core.DeviceManager
import com.tees.s3186984.whereabout.model.Connection
import com.tees.s3186984.whereabout.model.ConnectionRequest
import com.tees.s3186984.whereabout.model.ConnectionStatus
import com.tees.s3186984.whereabout.model.Device
import com.tees.s3186984.whereabout.model.User
import com.tees.s3186984.whereabout.repository.FireStoreRepository
import com.tees.s3186984.whereabout.repository.FirebaseAuthRepository
import com.tees.s3186984.whereabout.repository.LocalStoreRepository
import com.tees.s3186984.whereabout.wutils.CONNECTION
import com.tees.s3186984.whereabout.wutils.Helpers
import com.tees.s3186984.whereabout.wutils.NAME
import com.tees.s3186984.whereabout.wutils.QRCodeUtils
import com.tees.s3186984.whereabout.wutils.REQUEST_CODE_CAMERA
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(context: Context): ViewModel() {

    val firebaseRepo = FirebaseAuthRepository(context, viewModelScope)
    val fireStoreRepo = FireStoreRepository()
    val localStoreRepo = LocalStoreRepository(context)
    val currentUserId = firebaseRepo.getCurrentUser()?.uid ?: "" // TODO- shutdown gracefully without ID

    var currentUser: User? = null
        private set

    // Bottom Sheet Controls
    val sheetContent = mutableStateOf<@Composable () -> Unit>({})

    // Device Pairing Observable data
    var pairingRequestMessage =  mutableStateOf("")
    var deviceTagState = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    val qrCodeBitmap = mutableStateOf<Bitmap?>(null)

    // Scan process Result Observables
    private val _scannedResult = MutableStateFlow<String?>(null)
    val scannedResult: StateFlow<String?> get() = _scannedResult

    // Saving Connection Result Observables
    private val _connectionSaveStatus = MutableStateFlow<Result<Boolean>?>(null)
    val connectionSaveStatus: StateFlow<Result<Boolean>?> = _connectionSaveStatus


    val connectionRequestState = mutableStateOf<Pair<Boolean, ConnectionRequest?>>(Pair(false, null))
    val connectionErrorState = mutableStateOf(false)


    private val _cameraPermissionGranted = MutableStateFlow(false)
    val cameraPermissionGranted: StateFlow<Boolean> = _cameraPermissionGranted



    /*
    * Initialise essentials needed for this class
    */
    init {
        // Fetch the current user from Firestore when ViewModel is initialized
        viewModelScope.launch {
            fetchCurrentUser()
            generateQRCode()
        }
    }


    // TODO - This function should update the UI with error message if any of
    //  {currentUserId, currentUser.name, deviceId} is missing
    fun generateQRCode() {
        viewModelScope.launch{
            val connectionRequestData = DeviceManager.makeConnectionRequestData(
                currentUserId,
                currentUser?.name?: "",
                currentUser?.device?.deviceId!!
            )
            qrCodeBitmap.value = QRCodeUtils.generateQRCode(connectionRequestData)
        }
    }


    private suspend fun fetchCurrentUser() {
        isLoading.value = true
        try {
            val currentUserId = firebaseRepo.getCurrentUser()?.uid ?: return
            currentUser = fireStoreRepo.getSingleDocument<User>("user", currentUserId)

        } catch (e: Exception) {
            Log.d("HomeViewModel", "Error fetching current user: ${e.message}")
        }finally {
            isLoading.value = false
            Log.d("LOADED_DATA", "user: ${currentUser}")
        }
    }

    fun createConnection() {
        viewModelScope.launch{
            try {
                val currentUserFirstName = currentUser?.name?.split(" ")?.firstOrNull() ?: ""
                val (_, connection) = connectionRequestState.value

                // User device ID is saved to local store (using userID as key) on first time use of the App.
                // We would use this device Id to create the current user Device

                val currentUserDevice = DeviceManager.makeDevice(
                    deviceId = localStoreRepo.getStringPreference(currentUserId)
                )

                // Generate connection for initiator
                val initiatorConnection = Connection(
                    ownerId = connection?.initiatorId!!,
                    connectedUserId = currentUserId,
                    connectedUserFirstName = currentUserFirstName,
                    connectedUserDevice = currentUserDevice
                )

                // Generate connection for currentUser
                val currentUserConnection = Connection(
                    ownerId = currentUserId,
                    connectedUserId = connection.initiatorId,
                    connectedUserFirstName = connection.initiatorFirstName,
                    connectedUserDevice = connection.initiatorDevice,
                    tag = deviceTagState.value
                )

                val saveResult  = fireStoreRepo.saveMultipleDocuments(
                    CONNECTION, listOf(initiatorConnection, currentUserConnection))

                if (saveResult){
                    _connectionSaveStatus.value = Result.success(true)
                }else {
                    _connectionSaveStatus.value = Result.failure(Exception("Failed to save connections"))
                }

            }catch (e: Exception){
                Log.e("CreateConnection", "Error creating connection: ${e.message}")
                _connectionSaveStatus.value = Result.failure(e)
            }
        }


    }

    fun generatePairingRequest(){
        Log.d("HomeViewModel", "YES WE REACH")
        isLoading.value = true
        Log.d("HomeViewModel", "RESULT: ${_scannedResult.value}")
        val jsonString = _scannedResult.value ?: return

        try {
            val connectionRequest = DeviceManager.makeConnectionRequest(jsonString)
            Log.d("HomeViewModel", "name: ${connectionRequest.initiatorFirstName}")
            connectionRequestState.value = Pair(true, connectionRequest)
        }catch (e: Exception){
            Log.d("HomeViewModel", "Failed to generate pairing request: ${e.message}")
            connectionRequestState.value = Pair(false, null)
        }finally {
            isLoading.value = false
        }

    }


    fun requestCameraPermission(context: Context, launcher: ManagedActivityResultLauncher<String, Boolean>) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            launcher.launch(Manifest.permission.CAMERA)
        } else {
            _cameraPermissionGranted.value = true
        }
    }

    fun updatePermissionStatus(isGranted: Boolean) {
        _cameraPermissionGranted.value = isGranted
    }

    fun updateScannedResult(result: String) {
        _scannedResult.value = result
    }

    fun clearScannedResult() {
        _scannedResult.value = null
    }

}