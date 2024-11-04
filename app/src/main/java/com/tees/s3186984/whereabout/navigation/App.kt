package com.tees.s3186984.whereabout.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.ui.graphics.Color


import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.tees.s3186984.whereabout.view.auth.LogInScreen
import com.tees.s3186984.whereabout.view.launch.OnBoardingScreen
import com.tees.s3186984.whereabout.view.launch.SplashScreen
import com.tees.s3186984.whereabout.view.main.HomeScreen
import com.tees.s3186984.whereabout.viewmodel.LaunchViewModel
import com.tees.s3186984.whereabout.viewmodel.LogInViewModel


@Composable
fun App(context: Context){

    val navController = rememberNavController()
    val systemUiController = rememberSystemUiController()

    systemUiController.setStatusBarColor(color = Color.White, darkIcons = true)

    NavHost(navController = navController, startDestination = Screens.Splash.name) {
        composable(Screens.Splash.name) {
            val launchVM = LaunchViewModel(context)
            SplashScreen(navController = navController, launchVM=launchVM)
        }
        composable(Screens.Onboarding.name) { OnBoardingScreen(navController = navController) }

        composable(Screens.LogIn.name) {
            LogInScreen(navController = navController, logInVM = LogInViewModel())
        }

        composable(Screens.Home.name) { HomeScreen(navController = navController) }

        signUpNavGraph(navController)
    }
}

