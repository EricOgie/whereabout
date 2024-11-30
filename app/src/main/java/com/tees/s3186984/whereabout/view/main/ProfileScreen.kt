package com.tees.s3186984.whereabout.view.main

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.tees.s3186984.whereabout.model.User
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tees.s3186984.whereabout.componets.WLoadingBar
import com.tees.s3186984.whereabout.componets.WSmallFAB
import com.tees.s3186984.whereabout.navigation.Screens
import com.tees.s3186984.whereabout.ui.theme.WBackgroundGray
import com.tees.s3186984.whereabout.viewmodel.ProfileViewModel
import com.tees.s3186984.whereabout.wutils.LOGOUT
import com.tees.s3186984.whereabout.wutils.PossibleRequestState
import com.tees.s3186984.whereabout.wutils.SHARE_LOCATION_TITLE


@Composable
fun ProfileScreen(profileViewModel : ProfileViewModel, navController: NavController) {

    val requestState = profileViewModel.requestResultState.collectAsState().value
    val isLogOut = profileViewModel.logOutState.collectAsState().value

    // Navigate to Auth screen upon Logout
    if (isLogOut){
        navController.navigate(Screens.LogIn.name) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
            launchSingleTop = true
        }
    }

    when (requestState){
        is PossibleRequestState.ScreenLoading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                WLoadingBar()
            }
        }

        is PossibleRequestState.Failure -> {
            val errorMessage = requestState.message
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                   text = errorMessage,
                   style = MaterialTheme.typography.titleLarge,
                   color = Color.Black
                )
            }
        }

        is PossibleRequestState.SuccessWithData<User> -> {
            val user = requestState.data

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = WBackgroundGray),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(55.dp))
                // Profile Image
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )

                }

                Spacer(modifier = Modifier.height(16.dp))

                // User Information
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black,
                    fontSize = 28.sp
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Device Information
                Text(
                    text = "Device Info:",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    fontSize = 28.sp
                )
                Text(
                    text = "Device Name: ${user.device.deviceName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    fontSize = 20.sp
                )
                Text(
                    text = "Brand: ${user.device.brand}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    fontSize = 20.sp
                )
                Text(
                    text = "Model: ${user.device.model}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(120.dp))

                // Sign Out Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .align(alignment = Alignment.CenterHorizontally)
                        .clickable {

                        }
                        .padding(start = 0.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    WSmallFAB(
                        modifier = Modifier.padding(0.dp),
                        fabColor = Color.Blue.copy(0.7f),
                        contentColor = Color.White,
                        scaleFactor = 0.75f,
                        icon = Icons.AutoMirrored.Filled.Logout
                    ) { profileViewModel.logOut() }

                    Text(
                        modifier = Modifier.padding(start = 8.dp)
                            .clickable{ profileViewModel.logOut()},
                        color = Color.Blue.copy(0.7f),
                        text = LOGOUT,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

        }

        else -> {}
    }


}


