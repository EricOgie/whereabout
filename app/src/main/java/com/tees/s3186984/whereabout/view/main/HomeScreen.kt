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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
import com.tees.s3186984.whereabout.ui.theme.WhereaboutTheme
import com.tees.s3186984.whereabout.viewmodel.HomeViewModel
import com.tees.s3186984.whereabout.wutils.CRASH_ERROR
import com.tees.s3186984.whereabout.wutils.MapInteractivityState
import com.tees.s3186984.whereabout.wutils.PossibleRequestState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
) {

    val sheetState = rememberModalBottomSheetState()
    var isSheetOpened by rememberSaveable { mutableStateOf(false) }
    val screenRequestState by homeViewModel.requestResultState.collectAsState()
    val mapInteractiveState by homeViewModel.mapInteractiveState.collectAsState()

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
                        ShareLocationSheet(homeViewModel.pairedConnectionList)
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
            // Note - The value of mapInteractiveState emitted by handleLocationPinClick of GoogleMapView
            // floats from resolving (loading) to either a resolved-address-details (aka mapData) or an
            // address error with a message.
            //
            GoogleMapView(
                modifier = Modifier.fillMaxSize(),
                handleLocationPinClick = { mapInteractiveState ->
                    homeViewModel.updateMapInteractivityState(mapInteractiveState)
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
                    messageCount = 4,
                ){}
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
                        ShareLocationSheet(homeViewModel.pairedConnectionList)
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
                WLargeFAB{ Log.d("HOME-SCREEN", "QRCODE: yes") }

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




@Preview(showBackground = true)
@Composable
fun HomePreview() {
    WhereaboutTheme {
//        HomeScreen(navController = rememberNavController())
    }
}

