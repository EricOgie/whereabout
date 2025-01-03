package com.tees.s3186984.whereabout.componets

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.tees.s3186984.whereabout.R
import com.tees.s3186984.whereabout.model.Connection
import com.tees.s3186984.whereabout.model.ConnectionStatus
import com.tees.s3186984.whereabout.ui.theme.WBlack75
import com.tees.s3186984.whereabout.ui.theme.WBlack90
import com.tees.s3186984.whereabout.ui.theme.WGreen
import com.tees.s3186984.whereabout.ui.theme.WMeRed
import com.tees.s3186984.whereabout.ui.theme.WRed75
import com.tees.s3186984.whereabout.ui.theme.WhereaboutTheme
import com.tees.s3186984.whereabout.wutils.NO_TAG

/**
 * WScreenAddOn - A custom component for displaying a question with a clickable link.
 *
 * This composable displays a question and a clickable link underneath, which navigates
 * to a specified destination when clicked. This is useful for screens that require
 * additional guidance or optional navigation for further information.
 *
 * @param navController The NavController used to handle navigation within the app.
 * @param questionText The question text to be displayed at the top of the component.
 * @param linkTitle The clickable link text displayed below the question, usually
 * providing an option to navigate elsewhere.
 * @param linkDestination The route destination to navigate to when the link is clicked.
 *
 */
@Composable
fun WSignInOrSignUpAddOn(
    navController: NavController,
    questionText: String,
    linkTitle: String,
    linkDestination: String
) {

    Spacer(modifier = Modifier.height(5.dp))

    Text(
        modifier = Modifier
            .padding(0.dp),
        color = Color.Black,
        text = questionText,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily(Font(R.font.montserrat)),
        fontSize = 15.sp
    )

    Text(
        modifier = Modifier
            .padding(0.dp)
            .clickable {
                navController.navigate(linkDestination) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
            },

        color = WMeRed,
        text = linkTitle,
        textAlign = TextAlign.Center,

        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily(Font(R.font.montserrat)),
        fontSize = 13.sp
    )
}


@Composable
fun PairedConnectionItem(
  modifier: Modifier = Modifier,
  pairedConnection : Connection,
  handleDeleteClick: (Connection) -> Unit,
  handleEditClick: (Connection) -> Unit
){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    )
    {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
        {
            //-------------------- Phone Icon Section ---------------------- //
            Column(
                modifier = Modifier
                    .weight(0.2f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.phone),
                    contentDescription = null,
                    modifier = Modifier.size(70.dp)
                )
            }


            // ---------------------- Connection Information Section ----------//
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {

                val connectionTag = if (pairedConnection.tag.isEmpty()) NO_TAG
                                        else pairedConnection.tag
                Text(
                    modifier = Modifier.padding(0.dp),
                    text = connectionTag,
                    color = WBlack90,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 14.sp
                )

                Text(
                    modifier = Modifier.padding(0.dp),
                    text = "Device - ${pairedConnection.connectedUserDevice.deviceName}",
                    color = WBlack75,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 14.sp

                )

                Text(
                    modifier = Modifier.padding(0.dp),
                    text = pairedConnection.connectedUserFirstName,
                    color = WBlack75,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    modifier = Modifier.padding(0.dp),
                    text = pairedConnection.status.name.uppercase(),
                    color = if (pairedConnection.status == ConnectionStatus.ACTIVE)
                        WGreen else WRed75,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 14.sp
                )

            }


            // ----------------- Modification Icons Section --------------- //
            Column(
                modifier = Modifier
                    .weight(0.2f)
                    .fillMaxHeight().padding(top = 8.dp, bottom = 8.dp, end = 10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                Icon(
                    imageVector = Icons.Filled.Cancel,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.clickable{handleDeleteClick(pairedConnection)}
                )

                Spacer(modifier= Modifier.height(25.dp))

                Icon(
                    imageVector = Icons.Filled.EditNote,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.clickable{ handleEditClick(pairedConnection) }
                )
            }
        }

    }

}


/**
 * A reusable composable for handling runtime permission requests in Jetpack Compose.
 *
 * This composable checks if the specified permission is already granted and invokes the provided
 * callback with the result. If the permission is not granted, it requests the permission from
 * the user and passes the result to the callback once the user responds.
 *
 * **Use Case:**
 * - To simplify runtime permission handling in a Compose UI.
 * - Can be used anywhere a single permission needs to be requested and its result processed.
 *
 * **Example Usage:**
 * ```
 * RequestPermissionHandler(
 *     permissionName = Manifest.permission.CAMERA,
 *     handlePermissionRequestResponse = { requestResponse ->
 *         // Do what you want with the permission request response, requestResponse.
 *     }
 * )
 * ```
 *
 * @param permissionName The name of the permission to be requested (e.g., Manifest.permission.CAMERA).
 * @param handlePermissionRequestResponse A callback invoked with the permission result.
 */
@Composable
fun RequestPermissionHandler(
    permissionName: String,
    handlePermissionRequestResponse: (Boolean) -> Unit
)
{
    val context = LocalContext.current
    // Launcher for handling permission request result
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ){ isPermissionGranted -> handlePermissionRequestResponse(isPermissionGranted) }

    // Upon composition, we want the permission check to fire once with no chance of
    // recomposition. Hence, the reason for passing the static key, Unit, to the launchEffect() here
    LaunchedEffect(Unit) {
        // Check if the passed permissionName has been granted and pass true to the callback
        if (ContextCompat.checkSelfPermission(context, permissionName) == PackageManager.PERMISSION_GRANTED){
            handlePermissionRequestResponse(true)
        }else {
            // Launch permission request since permission has not been granted
            permissionLauncher.launch(input = permissionName)
        }
    }
}



@Preview(showBackground = false)
@Composable
fun ScreenAddOnPreview() {
    WhereaboutTheme {
        PairedConnectionItem(
            pairedConnection = Connection(),
            handleEditClick = {},
            handleDeleteClick = {}
        )
    }
}