package com.tees.s3186984.whereabout.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.tees.s3186984.whereabout.wutils.PossibleRequestState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tees.s3186984.whereabout.model.Connection
import com.tees.s3186984.whereabout.model.ConnectionStatus
import com.tees.s3186984.whereabout.model.User
import com.tees.s3186984.whereabout.repository.FireStoreRepository
import com.tees.s3186984.whereabout.repository.LocalStoreRepository
import com.tees.s3186984.whereabout.wutils.CONNECTION
import com.tees.s3186984.whereabout.wutils.GENERIC_ERR_MSG
import com.tees.s3186984.whereabout.wutils.OWNERID
import com.tees.s3186984.whereabout.wutils.USER
import kotlinx.coroutines.launch


class PairedConnectionViewModel(context: Context): ViewModel(){

    val gson = Gson()
    // Repositories for Firestore and Local Storage
    val firestoreRepo = FireStoreRepository()
    val localStoreRepo = LocalStoreRepository(context = context)

    // State to hold composable content for bottom model drawer sheet
    val sheetContent = mutableStateOf<@Composable () -> Unit>({})

    // State to hold the list of paired connections
    private val _pairedConnectionList = mutableStateListOf<Connection>()
    val pairedConnectionList: SnapshotStateList<Connection> get() = _pairedConnectionList

    // State to manage the network request state (idle, loading, success, failure)
    private val _networkRequestState = mutableStateOf<PossibleRequestState<Connection>>(PossibleRequestState.Idle)
    val networkRequestState: State<PossibleRequestState<Connection>> get() = _networkRequestState

    var currentUser: User? = null
        private set

    // Initialize the ViewModel by fetching the cached user and paired connections
    init {
        viewModelScope.launch{
            getCachedUser()
            getUserPairedConnections()
        }
    }


    // ---------------------------- PAIRED CONNECTION SCREEN RELATED METHODS -------------------  //

    /**
     * Fetches the paired connections for the current user from Firestore.
     * Updates the _pairedConnectionList with the fetched data.
     */
    private suspend fun getUserPairedConnections(){
        _networkRequestState.value = PossibleRequestState.ScreenLoading
        try {
            currentUser?.userId?.let { userId ->
               val connectListResult = firestoreRepo.getDocuments<Connection>(
                    CONNECTION,
                    Pair(OWNERID, userId)
                )

                _pairedConnectionList.clear()
                _pairedConnectionList.addAll(connectListResult)

            }?: run {
                Log.d("GetCachedUser", "Error fetching User: null UserID")
            }

        }catch (e: Exception){
            e.printStackTrace()
        }finally {
            _networkRequestState.value = PossibleRequestState.Idle
        }

    }



    /**
     * Retrieves the cached user information from local storage and sets it to currentUser.
     * Uses Gson for JSON deserialization.
     */
    private suspend fun getCachedUser(){
        try {
            val userAsJson = localStoreRepo.getStringPreference(USER)
            currentUser = gson.fromJson(userAsJson, User::class.java)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }


    // ---------------------------- EXPOSED BOTTOM SHEET RELATED METHODS -------------------  //

    /**
     * Updates the tag of a paired connection in Firestore.
     * After successful update, the local paired connection list is updated.
     */
    fun updatePairConnectionTag(newTag: String, pairedConnectionID: String){

        _networkRequestState.value = PossibleRequestState.ComponentLoading
        val updateData = mapOf("tag" to newTag)

        viewModelScope.launch{
            try {
                val isSuccessfulUpdate = firestoreRepo.updateDocument(CONNECTION, pairedConnectionID, updateData)

                if (isSuccessfulUpdate) {
                    // Since successful, update local paired connection list
                    val indexOfUpdatedConnection = _pairedConnectionList.indexOfFirst {
                        connection -> connection.connectionId == pairedConnectionID
                    }

                    if (indexOfUpdatedConnection > -1 ){
                        _pairedConnectionList[indexOfUpdatedConnection] = _pairedConnectionList[indexOfUpdatedConnection]
                            .copy(tag = newTag)
                    }

                    _networkRequestState.value = PossibleRequestState.Success

                }else {
                    _networkRequestState.value = PossibleRequestState.Failure(GENERIC_ERR_MSG)
                }

            }catch (e: Exception){
                _networkRequestState.value = PossibleRequestState.Failure(GENERIC_ERR_MSG)
                e.printStackTrace()
            }
        }
    }


    /**
     * Deletes a paired connection from Firestore.
     * On successful deletion, the local list is updated by removing the connection.
     *
     * * Caveat:
     *      In a real-world application, most of this logic would ideally reside on a proper backend server
     *      rather than being handled in the client-side code. Handling such logic on the client:
     *      * - Increases the risk of security vulnerabilities.
     *      * - Leads to potential data inconsistencies due to race conditions or incomplete operations.
     *      * - Puts unnecessary responsibility on the client application.
     *
     *      This implementation is purely for demonstration purposes as we are constrained to use
     *      Firebase for this project, and it is not representative of production-level architecture.
     */
    fun deletePairedConnection(pairedConnectionID: String){
        _networkRequestState.value = PossibleRequestState.ComponentLoading

        viewModelScope.launch{

            try {
                val isSuccessfulDelete = firestoreRepo.deleteDocument(CONNECTION, pairedConnectionID)
                if (isSuccessfulDelete){
                    // Since delete is successful, we update local list of connection
                    val indexOfDeletedConnection = _pairedConnectionList.indexOfFirst{ connection ->
                        connection.connectionId == pairedConnectionID
                    }

                    // Mark the other end of this connection as broken
                    val deletedConnection = _pairedConnectionList[indexOfDeletedConnection]
                    markDeletedConnectionAsBroken(deletedConnection)

                    // Update local connection List so it can reflect on the UI real-time
                    _pairedConnectionList.removeAt(indexOfDeletedConnection)

                    // Update network request state
                    _networkRequestState.value = PossibleRequestState.Success
                }else {
                    _networkRequestState.value = PossibleRequestState.Failure(GENERIC_ERR_MSG)
                }
            }catch (e: Exception){
                _networkRequestState.value = PossibleRequestState.Failure(GENERIC_ERR_MSG)
                e.printStackTrace()
            }

        }
    }


    /**
     * Resets the network request state to idle.
     * This can be used after the request has completed to reset the UI state.
     */
    fun resetNetworkRequestState(){
        _networkRequestState.value = PossibleRequestState.Idle
    }


    /**
     * markDeletedConnectionAsBroken updates the status of a connection to "BROKEN"
     *      when a related connection is deleted.
     *
     * Caveat:
     * In a real-world application, this logic would ideally reside on a proper backend server
     * rather than being handled in the client-side code. Handling such logic on the client:
     * - Increases the risk of security vulnerabilities.
     * - Leads to potential data inconsistencies due to race conditions or incomplete operations.
     * - Puts unnecessary responsibility on the client application.
     *
     * This implementation is purely for demonstration purposes as we are constrained to use
     * Firebase for this project, and it is not representative of production-level architecture.
     */
    private fun markDeletedConnectionAsBroken(deletedConnection: Connection){
        requireNotNull(currentUser) { "Current user must not be null." }

        val conditions = listOf(
            "ownerId" to deletedConnection.connectedUserId,
            "connectedUserId" to currentUser!!.userId,
            "connectedUserFirstName" to currentUser!!.name.split(" ").first()
        )

        val updateData = mapOf("tag" to ConnectionStatus.BROKEN)

        viewModelScope.launch{
            try {
                firestoreRepo.updateDocumentByFields(
                    collectionName = CONNECTION,
                    conditions = conditions,
                    updateData = updateData
                )
            }catch (e: Exception){
                Log.d("markDeletedConnectionAsBroken", "Error: ${e.message}")
                e.printStackTrace()
            }
        }
    }

}




