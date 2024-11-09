package com.tees.s3186984.whereabout.view.main

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tees.s3186984.whereabout.componets.ConnectionDetailSheet
import com.tees.s3186984.whereabout.componets.DeleteConnectionSheet
import com.tees.s3186984.whereabout.componets.WPairedConnection
import com.tees.s3186984.whereabout.ui.theme.WBlack75
import com.tees.s3186984.whereabout.ui.theme.WLightGray
import com.tees.s3186984.whereabout.ui.theme.WhereaboutTheme
import com.tees.s3186984.whereabout.viewmodel.PairedConnectionViewModel
import com.tees.s3186984.whereabout.wutils.CONNECTION_TITLE
import com.tees.s3186984.whereabout.wutils.PossibleRequestState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PairedConnectionScreen(connectionVM: PairedConnectionViewModel){

    val sheetState = rememberModalBottomSheetState()
    var isSheetOpened by rememberSaveable { mutableStateOf(false) }

    fun resetState(){
        isSheetOpened = false
        connectionVM.resetNetworkRequestState()
    }

    Surface(modifier = Modifier.fillMaxSize(), color = WLightGray){
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(15.dp))
        {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = CONNECTION_TITLE,
                color = WBlack75,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )

            HorizontalDivider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(7.dp))

            LazyColumn {
                items(connectionVM.pairedConnectionList, key = { it.connectionId }){ connection ->
                    WPairedConnection(
                        pairedConnection = connection,
                        handleDeleteClick = { clickedItem ->
                            // Mount Deletion sheet on Bottom Modal Drawer
                            connectionVM.sheetContent.value = {
                                DeleteConnectionSheet(
                                    requestState = connectionVM.networkRequestState,
                                    connection = clickedItem,
                                    handleDelete = { pairedConnectionId ->
                                        connectionVM.deletePairedConnection(pairedConnectionId)
                                    },
                                    //Unload Bottom Modal Drawer
                                    handleCancel = { isSheetOpened = false }
                                )
                            }

                            // Open Bottom Modal Drawer
                            isSheetOpened = true
                        },

                        handleEditClick = { clickedItem ->
                            // Mount Connection Detail sheet on Bottom Modal Drawer
                            connectionVM.sheetContent.value = {
                                ConnectionDetailSheet(
                                    requestState = connectionVM.networkRequestState,
                                    connection = clickedItem,
                                    submitEditTag = { connectionId, newTag ->
                                        connectionVM.updatePairConnectionTag(newTag, connectionId)
                                    }
                                )
                            }

                            // Open Bottom Modal Drawer
                            isSheetOpened = true
                        },

                        modifier = Modifier.animateItem()
                    )
                }
            }

        }

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
                    connectionVM.sheetContent.value()
                }
            }
        }
    }

    // --------- Observe and respond to stateful data --------------- //
    val networkRequestState by connectionVM.networkRequestState

    when(networkRequestState){
        is PossibleRequestState.Success -> {
            resetState()
        }

        is PossibleRequestState.Failure -> {
            val errorMessage = (networkRequestState as PossibleRequestState.Failure).message
            Toast.makeText(LocalContext.current, errorMessage, Toast.LENGTH_SHORT).show()
            resetState()
        }

        else -> {}
    }

}





@Preview(showBackground = false)
@Composable
fun ScreenAddOnPreview() {
    val context = LocalContext.current
    WhereaboutTheme {
        PairedConnectionScreen(
            PairedConnectionViewModel(context)
        )
    }
}