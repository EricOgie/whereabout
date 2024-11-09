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
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.tees.s3186984.whereabout.model.Connection
import com.tees.s3186984.whereabout.ui.theme.WBlue5
import com.tees.s3186984.whereabout.ui.theme.WhereaboutTheme
import com.tees.s3186984.whereabout.wutils.CANCEL
import com.tees.s3186984.whereabout.wutils.CONFIRM
import com.tees.s3186984.whereabout.wutils.CONNECTION_TAG_NUDGE
import com.tees.s3186984.whereabout.wutils.CON_DETAILS
import com.tees.s3186984.whereabout.wutils.DELETE
import com.tees.s3186984.whereabout.wutils.DEVICES
import com.tees.s3186984.whereabout.wutils.EDIT_CON_TAG
import com.tees.s3186984.whereabout.wutils.EXAMPLE_TAG
import com.tees.s3186984.whereabout.wutils.EXTRA_INFO
import com.tees.s3186984.whereabout.wutils.PossibleRequestState
import com.tees.s3186984.whereabout.wutils.SAVE


@Composable
fun ConnectionDetailSheet(
    requestState:State<PossibleRequestState<Connection>>,
    connection: Connection,
    submitEditTag: (String, String) -> Unit,
)
{
    val requestState by requestState
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


@Composable
fun DeleteConnectionSheet(
    requestState:State<PossibleRequestState<Connection>>,
    connection: Connection,
    handleDelete: (String) -> Unit,
    handleCancel: () -> Unit
)
{
    val requestState by requestState

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
        val connectionIdentity = if (connection.tag.isNotEmpty()) connection.tag else connection.connectedUserFirstName
        Text(
            textAlign = TextAlign.Center,
            text = "$CONFIRM delete $connectionIdentity",
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

            // --- Delete Button -- //
            if (requestState is PossibleRequestState.ComponentLoading){
                WLoadingBar()
            }else {
                TextButton(onClick = { handleDelete(connection.connectionId) })
                {
                    Text(
                        text = DELETE,
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

    }
}