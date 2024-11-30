package com.tees.s3186984.whereabout.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tees.s3186984.whereabout.ui.theme.WBlack75
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.tees.s3186984.whereabout.model.Connection
import com.tees.s3186984.whereabout.ui.theme.WBlue5
import com.tees.s3186984.whereabout.ui.theme.WhereaboutTheme
import com.tees.s3186984.whereabout.wutils.AFFIRM
import com.tees.s3186984.whereabout.wutils.CANCEL
import com.tees.s3186984.whereabout.wutils.CONNECTION_TAG_NUDGE
import com.tees.s3186984.whereabout.wutils.CON_DETAILS
import com.tees.s3186984.whereabout.wutils.DEVICES
import com.tees.s3186984.whereabout.wutils.EDIT_CON_TAG
import com.tees.s3186984.whereabout.wutils.EXAMPLE_TAG
import com.tees.s3186984.whereabout.wutils.EXTRA_INFO
import com.tees.s3186984.whereabout.wutils.PossibleRequestState
import com.tees.s3186984.whereabout.wutils.SAVE
import kotlinx.coroutines.flow.StateFlow

/**
 * A Composable function that displays a detailed sheet about a specific connection,
 * allowing users to view connection details and edit the connection's tag.
 *
 * @param requestState A [StateFlow] representing the current state of the request to update
 *                     the connection's tag. Used to display error messages or a loading indicator
 *                     when the request is in progress.
 * @param connection A [Connection] object containing the details of the connection to be displayed.
 *                   This includes information such as the connected user's name, device name, and model.
 * @param submitEditTag A lambda function invoked when the user submits an edited tag.
 *                      It receives the connection ID and the new tag value as arguments.
 */
@Composable
fun ConnectionDetailSheet(
    requestState:StateFlow<PossibleRequestState<Connection>>,
    connection: Connection,
    submitEditTag: (String, String) -> Unit,
)
{
    val requestState by requestState.collectAsState()
    var inputTextState = remember { mutableStateOf(connection.tag) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ){
        if(requestState is PossibleRequestState.Failure){
            val errorMessage = (requestState as PossibleRequestState.Failure).message
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 0.dp),
                color = Color.Red,
                text = "Error: $errorMessage",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }

        Text(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp),
            color = Color.Black.copy(0.95f),
            text = CON_DETAILS,
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Normal
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            text = DEVICES,
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
            text = "Name: ${connection.connectedUserFirstName}",
            style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Normal)
        )

        Text(
            modifier = Modifier.padding(start = 8.dp),
            color = WBlack75,
            text = "Device Name: ${connection.connectedUserDevice.deviceName}",
            style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Normal)
        )

        Text(
            modifier = Modifier.padding(start = 8.dp),
            color = WBlack75,
            text = "Model: ${connection.connectedUserDevice.model}",
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
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
        ) {

            WTextField(
                modifier = Modifier.align(Alignment.CenterVertically),
                state = inputTextState,
                label = EXAMPLE_TAG,
                leadingIcon = null,
            ) { value -> inputTextState.value  = value}
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

            val isSendEnabled = inputTextState.value.isNotBlank() && inputTextState.value != connection.tag
            if (requestState is PossibleRequestState.ComponentLoading){
                WLoadingBar()
            }else{
                TextButton(
                    onClick = {
                    submitEditTag(connection.connectionId, inputTextState.value)
                },
                    enabled = isSendEnabled,
                    modifier = Modifier.alpha(if (isSendEnabled) 1f else 0.5f)
                )
                {
                    Icon(
                        imageVector = Icons.Filled.Save,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        color = WBlack75,
                        text = SAVE,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal)
                    )
                }
            }

        }

    }

}



/**
 * A Composable function that displays a confirmation bottom sheet with a prompt message,
 * a cancel button, and a confirm action button. This component is designed to prompt the user
 * for confirmation before executing a critical action like Delete.
 *
 * @param requestState A [StateFlow] representing the current state of the confirmation request.
 *                     The state is used to determine if the confirm button should display a
 *                     loading indicator.
 * @param itemId The identifier of the item associated with the confirm action. This is passed
 *               to the confirm action handler when the user confirms.
 * @param confirmPromptMessage A message displayed to the user prompting them to confirm the action.
 * @param handleConfirmAction A lambda function triggered when the user clicks the confirm button.
 *                            It receives the [itemId] as an argument.
 * @param handleCancel A lambda function triggered when the user clicks the cancel button.
 */
@Composable
fun ConfirmBottomSheet(
    requestState:StateFlow<PossibleRequestState<Any>>,
    itemId: String,
    confirmPromptMessage: String,
    handleConfirmAction: (String) -> Unit,
    handleCancel: () -> Unit
)
{
    val requestState by requestState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp).background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {

        Icon(
            imageVector = Icons.Outlined.Warning,
            contentDescription = null,
            modifier = Modifier.size(75.dp),
            tint = Color.Red
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            textAlign = TextAlign.Center,
            text = confirmPromptMessage,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // --- Cancel Button -- //
            TextButton(onClick = { handleCancel() })
            {
                Text(
                    text = CANCEL,
                    color = WBlue5,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.width(30.dp))

            // --- Confirm Action Button -- //
            if (requestState is PossibleRequestState.ComponentLoading){
                WLoadingBar()
            }else {
                TextButton(onClick = { handleConfirmAction(itemId) })
                {
                    Text(
                        text = AFFIRM,
                        color = Color.Red,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

        }

    }
}



@Preview(showBackground = false)
@Composable
fun YoPreview() {
    WhereaboutTheme {
        // For Mounting view component for preview
    }
}