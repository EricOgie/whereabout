package com.tees.s3186984.whereabout.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tees.s3186984.whereabout.core.DeviceManager
import com.tees.s3186984.whereabout.model.Connection
import com.tees.s3186984.whereabout.model.ConnectionRequest
import com.tees.s3186984.whereabout.model.User
import com.tees.s3186984.whereabout.repository.FireStoreRepository
import com.tees.s3186984.whereabout.repository.FirebaseAuthRepository
import com.tees.s3186984.whereabout.repository.LocalStoreRepository
import com.tees.s3186984.whereabout.wutils.CONNECTION
import com.tees.s3186984.whereabout.wutils.QRCodeUtils
import com.tees.s3186984.whereabout.wutils.USER
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class HomeViewModel(context: Context): ViewModel() {

    val gson = Gson()
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

            // Cache user details in local store
            val userJsonString = gson.toJson(currentUser)
            localStoreRepo.saveStringPreference(USER, userJsonString)

        } catch (e: Exception) {
            Log.d("HomeViewModel", "Error fetching current user: ${e.message}")
        }finally {
            isLoading.value = false
            Log.d("LOADED_DATA", "user: ${currentUser}")
        }
    }

    fun createPairingConnection() {
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
                    connectionId = UUID.randomUUID().toString(),
                    ownerId = connection?.initiatorId!!,
                    connectedUserId = currentUserId, connectedUserFirstName = currentUserFirstName,
                    connectedUserDevice = currentUserDevice
                )

                // Generate connection for currentUser
                val currentUserConnection = Connection(
                    connectionId = UUID.randomUUID().toString(), ownerId = currentUserId,
                    connectedUserId = connection.initiatorId, connectedUserFirstName = connection.initiatorFirstName,
                    connectedUserDevice = connection.initiatorDevice, tag = deviceTagState.value
                )

                // Prepare a list of document ID to data pairs for batch saving to Firestore.
                // Each pair consists of a unique connectionId (used as the document ID)
                // and its corresponding connection data object.
                val ownerAndRecipientConnections = listOf(
                    initiatorConnection.connectionId to initiatorConnection,
                    currentUserConnection.connectionId to currentUserConnection
                )

                // Save connection Firestore
                val saveResult  = fireStoreRepo.saveMultipleDocuments(CONNECTION, ownerAndRecipientConnections)

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
        isLoading.value = true
        val jsonString = _scannedResult.value ?: return

        try {
            val connectionRequest = DeviceManager.makeConnectionRequest(jsonString)
            connectionRequestState.value = Pair(true, connectionRequest)
        }catch (e: Exception){
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