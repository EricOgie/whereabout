package com.tees.s3186984.whereabout.view.launch

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue

import androidx.navigation.NavController
import androidx.compose.ui.text.font.FontFamily

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tees.s3186984.whereabout.R
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import androidx.compose.ui.text.font.Font
import com.tees.s3186984.whereabout.viewmodel.LaunchViewModel

import com.tees.s3186984.whereabout.wutils.APP_NAME_COLORED


@Composable
fun SplashScreen(navController: NavController, launchVM: LaunchViewModel){

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splashlogo))
    val isReadyToNavigate by launchVM.isReadyToNavigate.collectAsState()

    LaunchedEffect(isReadyToNavigate) {

        if (isReadyToNavigate){
            navController.navigate(launchVM.navigationDestination) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White){
        Box(modifier = Modifier.fillMaxSize()){

            LottieAnimation(
                modifier = Modifier
                    .size(300.dp)
                    .align(Alignment.Center),
                composition = composition
            )

            Text(
                modifier = Modifier
                    .padding(30.dp)
                    .align(Alignment.BottomCenter),
                color = Color.Black,
                text = APP_NAME_COLORED,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily( Font(R.font.montserrat)),
                fontSize = 32.sp
            )

        }

    }

}





