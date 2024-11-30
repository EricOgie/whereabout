package com.tees.s3186984.whereabout.viewmodel

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.ImageBitmap
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
import com.tees.s3186984.whereabout.wutils.CRASH_ERROR
import com.tees.s3186984.whereabout.wutils.PossibleRequestState
import com.tees.s3186984.whereabout.wutils.QRCODE_ERROR
import com.tees.s3186984.whereabout.wutils.QRCodeUtils
import com.tees.s3186984.whereabout.wutils.CREATE_ERROR
import com.tees.s3186984.whereabout.wutils.MapInteractivityState
import com.tees.s3186984.whereabout.wutils.OWNERID
import com.tees.s3186984.whereabout.wutils.USER
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.Pair
import kotlin.String

class HomeViewModel(context: Context): ViewModel() {
    // Gson instance for JSON serialization and deserialization
    val gson = Gson()

    // Repositories for handling Firebase authentication, Firestore, and local storage
    val firebaseRepo = FirebaseAuthRepository(context, viewModelScope)
    val fireStoreRepo = FireStoreRepository()
    val localStoreRepo = LocalStoreRepository(context)

    // Holds the current user data retrieved from Firestore
    var currentUser: User? = null
        private set

    // Mutable state for controlling the bottom modal drawer content
    val bottomModalDrawerSheetContent = mutableStateOf<@Composable () -> Unit>({})

    // StateFlow to manage the network request result status
    private val _requestResultState = MutableStateFlow<PossibleRequestState<Any>>(
        PossibleRequestState.Idle)
    val requestResultState: StateFlow<PossibleRequestState<Any>> get() = _requestResultState

    // StateFlow to hold the generated QR code as an ImageBitmap
    private val _qRCodeState = MutableStateFlow<ImageBitmap?>(null)
    val qRCodeState: StateFlow<ImageBitmap?> get() = _qRCodeState

    private  val _mapInteractiveState = MutableStateFlow<MapInteractivityState>(
        MapInteractivityState.Idle
    )
    val mapInteractiveState: StateFlow<MapInteractivityState>
        get() = _mapInteractiveState

    // State to hold the list of paired connections
    private val _pairedConnectionList = mutableStateListOf<Connection>()
    val pairedConnectionList: SnapshotStateList<Connection> get() = _pairedConnectionList


    /*
    * Initialization block to fetch the current user and generate the QR code
    */
    init {
        viewModelScope.launch {
            fetchCurrentUser()
            generateQRCode()
            fetchPairedConnection()
        }
    }


    /*
     * Generates a QR code based on the current user and device's data
     */
    private fun generateQRCode() {
        viewModelScope.launch{
            val connectionRequestData = DeviceManager.makeConnectionRequestData(
                currentUser?.userId!!,
                currentUser?.name?: "",
                currentUser?.device?.deviceId!!
            )
            _qRCodeState.value = QRCodeUtils.generateQRCode(connectionRequestData)
        }
    }

    /*
     * Fetches the current user data from Firestore and caches it in local storage
     */
    private suspend fun fetchCurrentUser() {
        _requestResultState.value = PossibleRequestState.ScreenLoading
        try {
            val currentUserId = firebaseRepo.getCurrentUser()?.uid ?: throw IllegalStateException(CRASH_ERROR)
            currentUser = fireStoreRepo.getSingleDocument<User>("user", currentUserId)

            // Cache user details in local store
            val userJsonString = gson.toJson(currentUser)
            localStoreRepo.saveStringPreference(USER, userJsonString)
            resetRequestResultState()

        } catch (e: Exception) {
            _requestResultState.value = PossibleRequestState.Failure(e.message!!)
            e.printStackTrace()
        }
    }


    private suspend fun fetchPairedConnection() {
        try {
            val userPairedConn = fireStoreRepo.getDocuments<Connection>(
                CONNECTION, Pair(OWNERID, currentUser?.userId!!)
            )
            _pairedConnectionList.addAll(userPairedConn)

        }catch (e: Exception){
            e.printStackTrace()
        }
    }


    /*
     * Creates paired connections for both users involved in the pairing process
     */
    fun createPairedConnection(connectionTag: String) {
        viewModelScope.launch{
            try {
                val currentUserFirstName = currentUser?.name?.split(" ")?.firstOrNull() ?: ""
                // As at this point of this call, requestResultState should be PossibleRequestState.SuccessWithData
                // and the contained data should be a ConnectionRequest
                if (requestResultState.value is PossibleRequestState.SuccessWithData){
                    val connectionRequest = (requestResultState.value
                            as PossibleRequestState.SuccessWithData<*>).data
                            as ConnectionRequest

                    // User device ID is saved to local store (using userID as key) on first time use of the App.
                    // We would use this device Id to create the current user Device

                    val currentUserDevice = DeviceManager.makeDevice(
                        deviceId = localStoreRepo.getStringPreference(currentUser?.userId!!)
                    )

                    // Generate connection for initiator
                    val initiatorConnection = Connection(
                        connectionId = UUID.randomUUID().toString(),
                        ownerId = connectionRequest.initiatorId,
                        connectedUserId = currentUser?.userId!!,
                        connectedUserFirstName = currentUserFirstName,
                        connectedUserDevice = currentUserDevice
                    )

                    // Generate connection for currentUser
                    val currentUserConnection = Connection(
                        connectionId = UUID.randomUUID().toString(),
                        ownerId = currentUser?.userId!!,
                        connectedUserId = connectionRequest.initiatorId,
                        connectedUserFirstName = connectionRequest.initiatorFirstName,
                        connectedUserDevice = connectionRequest.initiatorDevice,
                        tag = connectionTag
                    )

                    // Prepare a list of document ID to data pairs for batch saving to Firestore.
                    // Each pair consists of a unique connectionId (used as the document ID)
                    // and its corresponding connection data object.
                    val ownerAndRecipientConnections = listOf(
                        initiatorConnection.connectionId to initiatorConnection,
                        currentUserConnection.connectionId to currentUserConnection
                    )

                    // We will now make request to firestore to save these connections
                        // First we will update _requestResultState to component loading
                    _requestResultState.value = PossibleRequestState.ComponentLoading
                    // Save connection Firestore
                    val saveResult  = fireStoreRepo.saveMultipleDocuments(CONNECTION, ownerAndRecipientConnections)

                    if (saveResult){
                        _requestResultState.value = PossibleRequestState.Success
                    }else {
                        _requestResultState.value = PossibleRequestState.Failure(CREATE_ERROR + CONNECTION)
                    }

                }

            }catch (e: Exception){
                _requestResultState.value = PossibleRequestState.Failure(CREATE_ERROR + CONNECTION)
                e.printStackTrace()
            }
        }

    }

    /*
     * Recreates a pairing request from a scanned QR code string
     */
    fun recreatePairingRequestFromQRCodeString(qRCodeJsonString: String){
        _requestResultState.value = PossibleRequestState.ComponentLoading
        try {
            val connectionRequest = gson.fromJson(qRCodeJsonString, ConnectionRequest::class.java)
            // Note - If this were to be a backend server, we would check the expiry of the connectionRequest here
            // and allow this process to fail with a message if connectionRequest expire
            _requestResultState.value = PossibleRequestState.SuccessWithData(connectionRequest)

        }catch (e: Exception){
            _requestResultState.value = PossibleRequestState.Failure(QRCODE_ERROR)
            e.printStackTrace()
        }

    }

    /*
     * Resets the request result state to idle
     */
    fun resetRequestResultState(){
        _requestResultState.value = PossibleRequestState.Idle
    }

    fun resetMapInteractiveState(){
        _mapInteractiveState.value = MapInteractivityState.Idle
    }

    fun updateMapInteractivityState(
        interactivityState: MapInteractivityState
    ){ _mapInteractiveState.value = interactivityState }


}