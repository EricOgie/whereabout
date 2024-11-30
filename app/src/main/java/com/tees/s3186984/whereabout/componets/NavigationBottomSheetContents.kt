package com.tees.s3186984.whereabout.componets


import android.Manifest
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.tees.s3186984.whereabout.wutils.Helpers
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.DevicesOther

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.tees.s3186984.whereabout.model.Connection
import com.tees.s3186984.whereabout.model.ConnectionRequest
import com.tees.s3186984.whereabout.ui.theme.WBlack75
import com.tees.s3186984.whereabout.ui.theme.WRed75
import com.tees.s3186984.whereabout.wutils.CAMERA_PERMISSION_ALERT
import com.tees.s3186984.whereabout.wutils.CONNECTION_TAG_NUDGE
import com.tees.s3186984.whereabout.wutils.CONNECTION_TITLE
import com.tees.s3186984.whereabout.wutils.CONNECT_INFO_TITLE
import com.tees.s3186984.whereabout.wutils.EDIT_CON_TAG
import com.tees.s3186984.whereabout.wutils.EXAMPLE_TAG
import com.tees.s3186984.whereabout.wutils.EXTRA_INFO
import com.tees.s3186984.whereabout.wutils.ImageAnalyser
import com.tees.s3186984.whereabout.wutils.MapInteractivityState
import com.tees.s3186984.whereabout.wutils.PAIRING_REVIEW_TITLE
import com.tees.s3186984.whereabout.wutils.PAIRING_TITLE
import com.tees.s3186984.whereabout.wutils.PLACES_TITLE
import com.tees.s3186984.whereabout.wutils.PossibleRequestState
import com.tees.s3186984.whereabout.wutils.SHARE_LOCATION_TITLE
import kotlinx.coroutines.flow.StateFlow

/**
 * Displays a container that shows a QR code with additional details.
 * Includes an image box, followed by device details and user information.
 */
@Composable
fun QRCodeContainer(
    username: String,
    userDeviceName: String,
    qRCodeState : StateFlow<ImageBitmap?>
)
{
    val qRCodeBitmapSate = qRCodeState.collectAsState().value

    if (qRCodeBitmapSate is ImageBitmap){
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
                    .background(color = Color.Gray),

                ){
                Image(
                    bitmap = qRCodeBitmapSate,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Text(
                text = PAIRING_TITLE,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = Helpers.capitalizeWords(userDeviceName),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = Helpers.capitalizeWords(username),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) { WLoadingBar(scaleFactor = 0.65f) }
    }






}

/**
 * Displays a QR code scanner interface with device and user information.
 * Includes a box for the QR code scanner, a title, and user details.
 *
 * NOTE: QRCodeScanner as defined here is more than a mare component (and it is not meant to be reusable)
 *  Its function can be likened to a fragment, or a mini screen scoped to the Home screen whose
 *      operation (scanning camera view) require a Viewmodel.
 *  Hence, the idiomatic rule of not passing viewmodel to a component does not really apply here
 */
@Composable
fun QRCodeScanner(
    handleOnCompleteQRCodeScan: (String) -> Unit,
    deviceName: String,
    userName: String
){

    var hasCameraPermissionState by remember { mutableStateOf(false)}

    // Request user's permission to use camera
    RequestPermissionHandler(permissionName =  Manifest.permission.CAMERA) { userResponse ->
        hasCameraPermissionState = userResponse
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        if (hasCameraPermissionState){
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .padding(5.dp)
                    .background(color = Color.Gray),
            ){
                CameraPreview(
                    modifier = Modifier.matchParentSize(),
                    onScanComplete = {scannedResult ->
                        handleOnCompleteQRCodeScan(scannedResult)
                    }
                )
            }
        }else{
            Text(text = CAMERA_PERMISSION_ALERT)
        }

        Spacer(modifier = Modifier.height(45.dp))

        Text(
            text = PAIRING_TITLE,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = deviceName,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = userName,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}


@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    onScanComplete: (String)-> Unit
)
{
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }


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
                                    onScanComplete(qrCodeResult)
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


@Composable
fun PairingPreviewSheet(
    requestState: StateFlow<PossibleRequestState<Any>>,
    onSubmit: (String) -> Unit
)
{
    val requestDataState = requestState.collectAsState().value


    when(requestDataState){
        is PossibleRequestState.ComponentLoading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){ WLoadingBar(scaleFactor = 0.5f)}
        }

        is PossibleRequestState.Failure -> {
            val errorMessage = requestDataState.message

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 0.dp),
                    color = Color.Red,
                    text = errorMessage,
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }

        is PossibleRequestState.SuccessWithData<*> -> {
            val pairConnection = requestDataState.data as ConnectionRequest
            val deviceTagState = remember { mutableStateOf("") }

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
                    text = PAIRING_REVIEW_TITLE,
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Normal
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.Black,
                    text = CONNECT_INFO_TITLE,
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Normal)
                )

                HorizontalDivider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    color = WBlack75,
                    text = "Name: ${pairConnection.initiatorFirstName}",
                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Normal)
                )

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    color = WBlack75,
                    text = "Device Name: ${pairConnection.initiatorDevice.deviceName}",
                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Normal)
                )

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    color = WBlack75,
                    text = "Model: ${pairConnection.initiatorDevice.model}",
                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Normal)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.Black,
                    text = EXTRA_INFO,
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
                    text = EDIT_CON_TAG,
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
                )

                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                ) {

                    WTextField(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        state = deviceTagState,
                        label = EXAMPLE_TAG,
                        leadingIcon = null,
                    ) { value -> deviceTagState.value  = value}
                }

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.Black.copy(0.75f),
                    text = CONNECTION_TAG_NUDGE,
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal)
                )


                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){

                    TextButton(onClick = {
                        onSubmit(deviceTagState.value)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.DevicesOther,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            color = WBlack75,
                            text = "Connect",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal)
                        )
                    }
                }

            }

        }

        else -> {}
    }

}


/**
 * Displays a sheet containing location details.
 * Shows a title, address information, and other components.
 */
@Composable
fun LocationDetailsSheet(
    addressState: StateFlow<MapInteractivityState>,
    handleShareButtonClick: () -> Unit
){
    val mapInteractiveMapState by addressState.collectAsState()
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


        when(val state = mapInteractiveMapState){
            is MapInteractivityState.Resolving -> {
                Spacer(modifier = Modifier.height(40.dp))
                WLoadingBar(scaleFactor = 0.25f)
            }

            is MapInteractivityState.Error -> {
                val errorMessage = (mapInteractiveMapState as MapInteractivityState.Error).message
                Spacer(modifier = Modifier.height(40.dp))
                Text(text = errorMessage, fontSize = 14.sp, color = WRed75)
            }

            is MapInteractivityState.MapData -> {
                val locationAddress = state.data
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp))
                {
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        color = Color.Black.copy(0.75f),
                        text = "Near ${locationAddress.address}",
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
                            .clickable { handleShareButtonClick() }
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
                            text = SHARE_LOCATION_TITLE,
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
                        text = PLACES_TITLE,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )

                    LazyColumn {
                        items(locationAddress.landmarks){ landmark ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(0.09f)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = landmark.name?: "",
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                        ),
                                        color = Color.Black
                                    )

                                    HorizontalDivider(
                                        color = Color.Gray,
                                        thickness = 1.dp,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    // Address text
                                    Text(
                                        text = landmark.address?: "",
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Normal
                                        ),
                                        color = Color.Black.copy(alpha = 0.75f)
                                    )
                                }
                            }
                        }
                    }

                }
            }

            else -> {}
        }

    }
}

/**
 * Displays a sheet containing real-time location sharing information.
 * Shows a share time duration, and devices to share location with.
 */
@Composable
fun ShareLocationSheet(connections: List<Connection>) {
    val selectedConnections: MutableList<String> = mutableListOf()

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
                text = SHARE_LOCATION_TITLE,
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

        // ------------ User Connection Section------------------- //
            Text(
                modifier = Modifier.padding(start = 8.dp),
                color = Color.Black.copy(0.95f),
                text = CONNECTION_TITLE,
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
            )
            {
                LazyRow {
                    items(connections){ connection ->
                        WSelectableCircularImage(
                            id = connection.connectionId,
                            title = if (connection.tag.isEmpty()) connection.connectedUserId else connection.tag
                        ) { connectionIde ->
                            if (selectedConnections.contains(connectionIde)){
                                selectedConnections.remove(connectionIde)
                            }else {
                                selectedConnections.add(connectionIde)
                            }
                        }
                    }
                }
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















