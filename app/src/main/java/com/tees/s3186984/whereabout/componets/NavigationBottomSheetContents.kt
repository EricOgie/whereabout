package com.tees.s3186984.whereabout.componets


import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

import androidx.camera.core.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.tees.s3186984.whereabout.viewmodel.HomeViewModel
import com.tees.s3186984.whereabout.wutils.Helpers

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis

import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.material.icons.filled.DevicesOther

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment

import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.tees.s3186984.whereabout.navigation.Screens
import com.tees.s3186984.whereabout.wutils.ImageAnalyser
import kotlin.Int

/**
 * Displays a container that shows a QR code with additional details.
 * Includes an image box, followed by device details and user information.
 */
@Composable
fun QRCodeContainer(homeViewModel: HomeViewModel){

    val qrCodeBitmap = homeViewModel.qrCodeBitmap.value
   Column(
       modifier = Modifier
           .fillMaxSize()
           .padding(5.dp),
       verticalArrangement = Arrangement.Center,
       horizontalAlignment = Alignment.CenterHorizontally
   )
   {
       Box(
           modifier = Modifier
               .size(250.dp)
               .padding(5.dp)
               .background(color = Color.Gray),

       ){
           qrCodeBitmap?.let {
               Image(
                   bitmap = it.asImageBitmap(),
                   contentDescription = null,
                   modifier = Modifier.fillMaxSize()
               )
           }?: run {
               // Fallback or loading state (can add a progress indicator here)
               Text(text = "") // TODO - Show a loading bar
           }
       }

       Spacer(modifier = Modifier.height(12.dp))

       Text(
           text = Helpers.capitalizeWords(homeViewModel.currentUser!!.device.deviceName),
           style = TextStyle(
               fontSize = 16.sp,
               fontWeight = FontWeight.Normal
           )
       )

       Spacer(modifier = Modifier.height(8.dp))

       Text(
           text = Helpers.capitalizeWords(homeViewModel.currentUser!!.name),
           style = TextStyle(
               fontSize = 20.sp,
               fontWeight = FontWeight.Bold
           )
       )
   }
}

/**
 * Displays a QR code scanner interface with device and user information.
 * Includes a box for the QR code scanner, a title, and user details.
 */
@Composable
fun QRCodeScanner(
    navController: NavController,
    homeViewModel: HomeViewModel,
    context: Context,
    navBarIndexState: MutableState<Int>
){
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
            homeViewModel.updatePermissionStatus(isGranted)
    }

    LaunchedEffect(Unit) { homeViewModel.requestCameraPermission(context, permissionLauncher) }

    val cameraPermissionGranted = homeViewModel.cameraPermissionGranted.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        if (cameraPermissionGranted){
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .padding(5.dp)
                    .background(color = Color.Gray),
            ){
                CameraPreview(
                    modifier = Modifier.matchParentSize(),
                    context = context,
                    homeViewModel = homeViewModel,
                    navController = navController,
                    navBarIndexState = navBarIndexState
                )
            }
        }else{
            Text("Camera permission is required to scan QR codes.")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Pairing As",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Samsung Galaxy S10, 2018",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Eric Ogie Aghahowa",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

/**
 * Displays a sheet containing location details.
 * Shows a title, address information, and other components.
 */
@Composable
fun LocationDetailsSheet(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ){
        Text(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp),
            color = Color.Black.copy(0.95f),
            text = "Your Location",
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Normal
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black.copy(0.75f),
            text = "Near 7, Somerset Street, Middlesbrough",
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable {}
                .padding(start = 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            WSmallFAB(
                modifier = Modifier.padding(0.dp),
                fabColor = Color.Blue.copy(0.7f),
                contentColor = Color.White,
                scaleFactor = 0.65f,
                icon = Icons.Filled.Share
            ) {  }

            Text(
                modifier = Modifier.padding(start = 8.dp),
                color = Color.Blue.copy(0.7f),
                text = "Share your Location",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black.copy(0.95f),
            text = "Nearby Landmarks",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            )
        )

        Card(

            colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(0.09f)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Town Hall",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color.Black
                )

                HorizontalDivider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Address text
                Text(
                    text = "1, Florent Street, Inglebly Barwick, Stockton-on-Tees",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    color = Color.Black.copy(alpha = 0.75f)
                )
            }
        }
    }
}

/**
 * Displays a sheet containing real-time location sharing information.
 * Shows a share time duration, and devices to share location with.
 */
@Composable
fun ShareLocationSheet() {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp),
        ){
            // ----------- Sheet Title ------------------------- //
            Text(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp),
                color = Color.Black.copy(0.95f),
                text = "Share real-time location",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal)
            )

            Spacer(modifier = Modifier.height(15.dp))
            // --------------- Share Time Section --------------  //
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    WRadioButton(
                        modifier = Modifier.padding(0.dp),
                        selected = true,
                        onSelect = {},
                        size = 40f
                    )

                    Column{
                        Text(
                            text = "For 2 hours", color = Color.Black,
                            fontSize = 14.sp

                        )
                        Text(
                            text = "Until 13.15",
                            color = Color.Gray,
                            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal),
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.End) {

                    WSmallFAB(
                        fabColor = Color.Blue.copy(0.8f),
                        contentColor = Color.White,
                        scaleFactor = 0.55f,
                        icon = Icons.Filled.Add
                    ) {  }

                    WSmallFAB(
                        fabColor = Color.Red.copy(0.8f),
                        contentColor = Color.White,
                        scaleFactor = 0.55f,
                        icon = Icons.Filled.Remove
                    ) {  }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                WRadioButton(
                    modifier = Modifier.padding(0.dp),
                    selected = false,
                    onSelect = {},
                    size = 40f
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Until you turn sharing off",
                    color = Color.Black,
                    fontSize = 14.sp
                )
            }
        }


        HorizontalDivider(
            color = Color.Gray.copy(0.8f),
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 5.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp),
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.padding(start = 8.dp),
                color = Color.Black.copy(0.95f),
                text = "Your Connection",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // TODO -- REPLACE dummy data with lazyColumn --//
                WSelectableCircularImage(
                    id = "1twhwt7",
                    title = "Mom's Phone"
                ) { }

                WSelectableCircularImage(
                    id = "1twhwt7",
                    title = "James's Phone"
                ) { }

                WSelectableCircularImage(
                    id = "1twhwt7",
                    title = "Grace",
                    isSelected = true
                ) { }

            }
        }

        HorizontalDivider(
            color = Color.Gray.copy(0.8f),
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 5.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            horizontalArrangement = Arrangement.Center
        ){
            TextButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    color = Color.Blue.copy(0.75f),
                    text = "Share",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal)
                )
            }
        }


    }

}


@Composable
fun PairingPreviewSheet(
    navController: NavController,
    homeViewModel: HomeViewModel,
    navBarIndexState: MutableState<Int>
) {

    if (homeViewModel.isLoading.value){
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){ WLoadingBar(scaleFactor = 3f)}

    }else {

        val (isSuccess, connectionRequest) = homeViewModel.connectionRequestState.value
        val connectError = homeViewModel.connectionErrorState.value
        if (!isSuccess || connectError){
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 0.dp),
                color = Color.Red,
                text = "Oops! Something went wrong",
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ){
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp),
                    color = Color.Black.copy(0.95f),
                    text = "Pairing Preview",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Normal
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.Black,
                    text = "Connection Information",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Normal)
                )

                HorizontalDivider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.Black.copy(0.75f),
                    text = "Name: ${connectionRequest?.initiatorFirstName}",
                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Normal)
                )

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.Black.copy(0.75f),
                    text = "Device Name: ${connectionRequest?.initiatorDevice?.deviceName}",
                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Normal)
                )

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.Black.copy(0.75f),
                    text = "Model: ${connectionRequest?.initiatorDevice?.model}",
                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Normal)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.Black,
                    text = "Extra Information",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Normal)
                )

                HorizontalDivider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )


                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.Black,
                    text = "Add Connection Tag",
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
                )

                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.padding(8.dp).fillMaxWidth(),
                ) {

                    WTextField(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        state = homeViewModel.deviceTagState,
                        label = "Example: Mom's Phone",
                        leadingIcon = null,
                    ) { value -> homeViewModel.deviceTagState.value  = value}
                }

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.Black.copy(0.75f),
                    text = "Connection tags are easy way to identify your connections",
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal)
                )


                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    TextButton(onClick = {
                        homeViewModel.createConnection()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.DevicesOther,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            color = Color.Blue.copy(0.75f),
                            text = "Connect",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal)
                        )
                    }
                }

            }
        }

    }

    // -------------  Observe Connection Request Result -------------------------- //

    val connectionSaveStatus by homeViewModel.connectionSaveStatus.collectAsState()

    LaunchedEffect(connectionSaveStatus) {
        connectionSaveStatus?.let { result ->
            result.onSuccess {
                navBarIndexState.value = 1
                navController.navigate(Screens.Connection.name){
                    popUpTo(navController.graph.startDestinationId){ saveState = true}
                    launchSingleTop = true
                }
            }.onFailure { exception ->
                homeViewModel.connectionErrorState.value = true
            }
        }
    }

}


@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    context: Context,
    homeViewModel: HomeViewModel,
    navController: NavController,
    navBarIndexState: MutableState<Int>
)
{
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }
    val scannedResult by homeViewModel.scannedResult.collectAsState()

    scannedResult?.let {
        homeViewModel.clearScannedResult()
        homeViewModel.sheetContent.value = {
            PairingPreviewSheet(
                navController = navController,
                homeViewModel = homeViewModel,
                navBarIndexState = navBarIndexState
            )
        }
    }

    AndroidView(
        factory = {
            previewView.apply {
                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(this.surfaceProvider)
                    }

                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()
                        .also{
                            it.setAnalyzer(
                                ContextCompat.getMainExecutor(context),
                                ImageAnalyser { qrCodeResult ->
                                    homeViewModel.updateScannedResult(qrCodeResult)
                                    homeViewModel.generatePairingRequest()
                                    cameraProvider.unbindAll()
                                }
                            )
                        }

                    val cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalysis
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(context))
            }
        },
        modifier = modifier
    )
}









