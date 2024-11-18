package com.tees.s3186984.whereabout.view.main

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.QrCode2
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tees.s3186984.whereabout.componets.LocationDetailsSheet
import com.tees.s3186984.whereabout.componets.NotificationIconWithBadge
import com.tees.s3186984.whereabout.componets.QRCodeContainer
import com.tees.s3186984.whereabout.componets.QRCodeScanner
import com.tees.s3186984.whereabout.componets.ShareLocationSheet
import com.tees.s3186984.whereabout.componets.WLargeFAB
import com.tees.s3186984.whereabout.componets.WSmallFAB
import com.tees.s3186984.whereabout.ui.theme.WBackgroundGray
import com.tees.s3186984.whereabout.ui.theme.WhereaboutTheme
import com.tees.s3186984.whereabout.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
    context: Context,
    navBarIndexState: MutableState<Int>
) {

    val sheetState = rememberModalBottomSheetState()
    var isSheetOpened by rememberSaveable { mutableStateOf(false) }

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

            // ------------ Notification Badge ----------------- //
            NotificationIconWithBadge(
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.TopEnd),
                messageCount = 4,
            ){}

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
                    homeViewModel.sheetContent.value = { ShareLocationSheet() }
                    isSheetOpened = true
                }

                Spacer(modifier = Modifier.height(10.dp))

                // ---------------- QRCode Scanner Floating Action Bar -------- //
                WSmallFAB(
                    fabColor = Color.White,
                    contentColor = Color.Black,
                    icon = Icons.Outlined.QrCodeScanner
                ) {
                    homeViewModel.sheetContent.value = {
                        QRCodeScanner(navController, homeViewModel, context, navBarIndexState = navBarIndexState)
                    }
                    isSheetOpened = true
                }

                Spacer(modifier = Modifier.height(10.dp))

                // ---------------- SOS Floating Action Bar -------- //
                WLargeFAB{ Log.d("HOME-SCREEN", "QRCODE: yes") }

                Spacer(modifier = Modifier.height(10.dp))

                // ---------------- Device Pairing QRCode Floating Action Bar -------- //
                WSmallFAB(
                    fabColor = Color.White,
                    contentColor = Color.Black.copy(1f),
                    icon = Icons.Outlined.QrCode2
                ) {
                    homeViewModel.sheetContent.value = { QRCodeContainer(homeViewModel = homeViewModel)}
                    isSheetOpened = true
                }

                Spacer(modifier = Modifier.height(10.dp))

                // ---------------- WHERE AM I FAB ------------- //
                WSmallFAB(
                    fabColor = Color.Blue.copy(0.85f),
                    contentColor = Color.White,
                    icon = Icons.Filled.LocationOn
                ) {
                    homeViewModel.sheetContent.value = { LocationDetailsSheet() }
                    isSheetOpened = true
                }


                Spacer(modifier = Modifier.height(80.dp))
            }

        }

        // --------------- Bottom Modal Sheet ---------------------- //
        if (isSheetOpened){
            ModalBottomSheet(
                onDismissRequest = {isSheetOpened = false},
                containerColor = Color.White,
                sheetState = sheetState,
                scrimColor = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    homeViewModel.sheetContent.value()
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