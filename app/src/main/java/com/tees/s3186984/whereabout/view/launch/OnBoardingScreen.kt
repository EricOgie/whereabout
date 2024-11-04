package com.tees.s3186984.whereabout.view.launch


import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import com.airbnb.lottie.compose.LottieConstants



import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.tees.s3186984.whereabout.R
import com.tees.s3186984.whereabout.componets.ScreenAddOn
import com.tees.s3186984.whereabout.componets.SubmitButton
import com.tees.s3186984.whereabout.navigation.Screens
import com.tees.s3186984.whereabout.ui.theme.WhereaboutTheme
import com.tees.s3186984.whereabout.wutils.ALREADY_HAVE


import com.tees.s3186984.whereabout.wutils.LOGIN
import com.tees.s3186984.whereabout.wutils.ONBOARDING_MSG
import com.tees.s3186984.whereabout.wutils.REGISTER
import com.tees.s3186984.whereabout.wutils.WELCOME

@Composable
fun OnBoardingScreen(navController: NavController) {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splashlogo))


    Surface(modifier = Modifier.fillMaxSize(), color = Color.White){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 4.dp, horizontal = 12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(modifier = Modifier.height(20.dp))

            LottieAnimation(
                modifier = Modifier
                    .size(250.dp),
                composition = composition,
                iterations = LottieConstants.IterateForever
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                modifier = Modifier
                    .padding(3.dp),
                color = Color.Black,
                text = WELCOME,
                style = MaterialTheme.typography.titleLarge,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily( Font(R.font.montserrat)),
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                modifier = Modifier
                    .padding(15.dp),
                color = Color.Black,
                text = ONBOARDING_MSG,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily( Font(R.font.montserrat)),
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(30.dp))
            SubmitButton(
                modifier = Modifier.padding(10.dp),
                text = REGISTER) {

                navController.navigate(Screens.SignUp.name){
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
            }

            ScreenAddOn(navController, ALREADY_HAVE, LOGIN, Screens.LogIn.name)

        }




    }

}



@Preview(showBackground = true)
@Composable
fun HomePreview() {
    WhereaboutTheme {
        OnBoardingScreen(navController= rememberNavController())
    }
}