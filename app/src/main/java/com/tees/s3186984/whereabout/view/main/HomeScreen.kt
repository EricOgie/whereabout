package com.tees.s3186984.whereabout.view.main

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BroadcastOnHome
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tees.s3186984.whereabout.componets.ConfirmBottomSheet
import com.tees.s3186984.whereabout.componets.GoogleMapView
import com.tees.s3186984.whereabout.componets.LocationDetailsSheet
import com.tees.s3186984.whereabout.componets.NotificationIconWithBadge
import com.tees.s3186984.whereabout.componets.PairingPreviewSheet
import com.tees.s3186984.whereabout.componets.QRCodeContainer
import com.tees.s3186984.whereabout.componets.QRCodeScanner
import com.tees.s3186984.whereabout.componets.ShareLocationSheet
import com.tees.s3186984.whereabout.componets.WLargeFAB
import com.tees.s3186984.whereabout.componets.WSmallFAB
import com.tees.s3186984.whereabout.navigation.Screens
import com.tees.s3186984.whereabout.ui.theme.WBackgroundGray
import com.tees.s3186984.whereabout.viewmodel.HomeViewModel
import com.tees.s3186984.whereabout.viewmodel.NotificationViewmodel
import com.tees.s3186984.whereabout.wutils.CONFIRM
import com.tees.s3186984.whereabout.wutils.CRASH_ERROR
import com.tees.s3186984.whereabout.wutils.MapInteractivityState
import com.tees.s3186984.whereabout.wutils.PossibleRequestState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
    notificationViewmodel: NotificationViewmodel
) {

    val sheetState = rememberModalBottomSheetState()
    var isSheetOpened by rememberSaveable { mutableStateOf(false) }
    val screenRequestState by homeViewModel.requestResultState.collectAsState()
    val mapInteractiveState by homeViewModel.mapInteractiveState.collectAsState()
    val isBroadcasting by homeViewModel.isBroadCasting.collectAsState()
    val notifications by notificationViewmodel.notifications.collectAsState()
    val context = LocalContext.current

    if (screenRequestState is PossibleRequestState.Failure){
        val failureMessage = (screenRequestState as PossibleRequestState.Failure).message

        // The only condition to get a crash error message is if the app could not fetch
        // the used data it require to function properly. When this happen, we want to
        // navigate to a dead screen with no return
        if (failureMessage == CRASH_ERROR){
            navController.navigate(Screens.Error.name){
                popUpTo(navController.graph.startDestinationId){inclusive = true}
            }
        }
    }

    when(screenRequestState){
        is PossibleRequestState.Success -> {
            isSheetOpened = false
            homeViewModel.resetRequestResultState()
            // Navigate to Paired Connection Screen
            navController.navigate(Screens.Connection.name){
                popUpTo(navController.graph.startDestinationId){ inclusive = true}
            }
        }

        is PossibleRequestState.Failure -> {
            val failureMessage = (screenRequestState as PossibleRequestState.Failure).message

            val toast = Toast.makeText(LocalContext.current, failureMessage, Toast.LENGTH_SHORT)
            toast.show()
            // reset screenRequestState
            homeViewModel.resetRequestResultState()
        }

        else -> {}
    }

    if (mapInteractiveState !is MapInteractivityState.Idle){
        homeViewModel.bottomModalDrawerSheetContent.value = {
            LocationDetailsSheet(
                addressState = homeViewModel.mapInteractiveState,
                handleShareButtonClick = {
                    // The share button is clicked, we want to mount the share bottom drawer sheet
                    homeViewModel.bottomModalDrawerSheetContent.value = {
                        ShareLocationSheet(
                            connections = homeViewModel.pairedConnectionList,
                            handleShareLocation = { duration, bucketList ->
                                homeViewModel.broadcastLocation(duration, bucketList)
                            }
                        )
                    }

                }
            )
        }
        isSheetOpened = true
    }



    // Parent Container
    Surface(
        modifier = Modifier.fillMaxSize()
    )
    {
        Box(
            modifier = Modifier
                .background(color = WBackgroundGray)
                .fillMaxSize()
        )
        {
            // -------------- Embedded Google Map Background -------------//

            GoogleMapView(
                modifier = Modifier.fillMaxSize(),

                handleLocationPinClick = { mapInteractiveState ->
                    // mapInteractiveState emitted here floats from resolving (loading) to either a
                    // resolved-address-details (aka mapData) or an address error with a message.
                    homeViewModel.updateMapInteractivityState(mapInteractiveState)
                },

                // When the user change location and the distance changed is up to 10Meters
                // this callback will be triggered with user new location as LatLng object.
                // The application keeps tabs on the user's current location by saving/updating this value
                // in firebase firestore
                handleOnLocationUpdate = { newLocation ->
                    homeViewModel.saveOrUpdateUserLocation(newLocation)
                }
            )


            // ------------ Notification Badge ----------------- //

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.TopEnd),
                horizontalAlignment = Alignment.End
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                NotificationIconWithBadge(
                    modifier = Modifier
                        .padding(30.dp),
                    messageCount = notifications.size,
                    handleClick = {
                        Toast.makeText(context, "You have ${notifications.size} notifications", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            // The below ui component will be visible when the current user
            // is sharing his/her location with paired connections
            if (isBroadcasting){
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.TopStart),
                    horizontalAlignment = Alignment.Start
                )
                {
                    WSmallFAB(
                        fabColor = Color.Red.copy(0.8f),
                        contentColor = Color.White,
                        icon = Icons.Filled.BroadcastOnHome,
                        handleClick = {
                            homeViewModel.bottomModalDrawerSheetContent.value = {
                                // Mount confirm bottom sheet and ask user to confirm
                                // if they want to stop sharing their location
                                ConfirmBottomSheet(
                                    requestState = homeViewModel.requestResultState,
                                    itemId = "", // ID Not necessary for this action
                                    confirmPromptMessage = "$CONFIRM Stop location Sharing?",
                                    handleCancel = { isSheetOpened = false },
                                    handleConfirmAction = {
                                        homeViewModel.stopLocationBroadCast()
                                    }

                                )
                            }
                        }
                    )
                }
            }



            // ----------- Side Feature Pane ------------------//
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentHeight()
                    .align(Alignment.BottomEnd)
                    .background(color = Color.Transparent),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                // ---------------- Share Location Floating Action Bar -------- //
                WSmallFAB(
                    fabColor = Color.Blue.copy(0.8f),
                    contentColor = Color.White,
                    icon = Icons.Filled.Share
                ) {
                    homeViewModel.bottomModalDrawerSheetContent.value = {
                        ShareLocationSheet(
                            connections = homeViewModel.pairedConnectionList,
                            handleShareLocation = { duration, bucketList ->
                                homeViewModel.broadcastLocation(
                                    broadcastDuration = duration,
                                    userBucketList = bucketList
                                )
                            }
                        )
                    }
                    isSheetOpened = true
                }

                Spacer(modifier = Modifier.height(30.dp))

                // ---------------- QRCode Scanner Floating Action Bar -------- //
                WSmallFAB(
                    fabColor = Color.White,
                    contentColor = Color.Black,
                    icon = Icons.Outlined.QrCodeScanner
                ) {
                    // Onclick of this button, we want to mount
                    // our custom QRCOdeScanner composable on
                    // ButtonModalDrawer and then fire the ButtonModalDrawer
                    homeViewModel.bottomModalDrawerSheetContent.value = {
                        val user  = homeViewModel.currentUser!!
                        QRCodeScanner(
                            userName = user.name,
                            deviceName = user.device.deviceName,
                            handleOnCompleteQRCodeScan = { qrCodeScannedResult ->
                                // We are using a nested composable here - When QRCodeScanner successfully scan
                                // any QRCode (Valid, malformed or invalid), we want to swap QRCodeScanner
                                // in our ButtonModalDrawer with PairingConnectionPreviewSheet. This will provide
                                // the mini-screen to preview the details of the scanned qRCode
                                homeViewModel.bottomModalDrawerSheetContent.value = {
                                    PairingPreviewSheet(
                                        requestState = homeViewModel.requestResultState,
                                        onSubmit = {tag -> homeViewModel.createPairedConnection(tag)}
                                    )
                                }

                                homeViewModel.recreatePairingRequestFromQRCodeString(qrCodeScannedResult)
                            }
                        )
                    }
                    isSheetOpened = true
                }

                Spacer(modifier = Modifier.height(30.dp))

                // ---------------- SOS Floating Action Bar -------- //
                WLargeFAB{
                    homeViewModel.bottomModalDrawerSheetContent.value = {
                        ShareLocationSheet(
                            connections = homeViewModel.pairedConnectionList,
                            handleShareLocation = { duration, bucketList ->
                                homeViewModel.broadcastLocation(
                                    broadcastDuration = duration,
                                    userBucketList = bucketList
                                )
                            }
                        )
                    }
                    isSheetOpened = true
                }

                Spacer(modifier = Modifier.height(30.dp))

                // ---------------- Device Pairing QRCode Floating Action Bar -------- //
                WSmallFAB(
                    fabColor = Color.Blue.copy(0.8f),
                    contentColor = Color.White,
                    icon = Icons.Filled.QrCode2
                ) {
                    homeViewModel.bottomModalDrawerSheetContent.value = {
                        QRCodeContainer(
                            username = homeViewModel.currentUser?.name!!,
                            userDeviceName = homeViewModel.currentUser?.device?.deviceName!!,
                            qRCodeState = homeViewModel.qRCodeState
                        )
                    }

                    isSheetOpened = true
                }

                Spacer(modifier = Modifier.height(80.dp))
            }

        }

        // --------------- Bottom Modal Sheet ---------------------- //
        if (isSheetOpened){
            ModalBottomSheet(
                onDismissRequest = {
                    homeViewModel.resetRequestResultState()
                    homeViewModel.resetMapInteractiveState()
                    isSheetOpened = false
                },
                containerColor = Color.White,
                sheetState = sheetState,
                scrimColor = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    homeViewModel.bottomModalDrawerSheetContent.value()
                }
            }
        }

    } // - End of Parent Container

} // - End of Composable Screen


