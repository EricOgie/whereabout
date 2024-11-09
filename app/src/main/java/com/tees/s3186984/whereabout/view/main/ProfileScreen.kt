package com.tees.s3186984.whereabout.view.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.tees.s3186984.whereabout.model.User
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tees.s3186984.whereabout.componets.WLoadingBar
import com.tees.s3186984.whereabout.ui.theme.WBackgroundGray
import com.tees.s3186984.whereabout.viewmodel.ProfileViewModel
import com.tees.s3186984.whereabout.wutils.PROFILE_IMAGE_PLACEHOLDER
import com.tees.s3186984.whereabout.wutils.PossibleRequestState
import java.net.HttpURLConnection
import java.net.URL


@Composable
fun ProfileScreen(profileViewModel : ProfileViewModel) {

    val requestState = profileViewModel.requestResultState.collectAsState().value
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
            }

        }

        else -> {}
    }




}

@Preview(showBackground = true)
@Composable
fun PreviewUserProfile() {
    ProfileScreen(ProfileViewModel(LocalContext.current))
}
