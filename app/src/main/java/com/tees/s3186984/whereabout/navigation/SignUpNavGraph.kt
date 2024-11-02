package com.tees.s3186984.whereabout.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.tees.s3186984.whereabout.view.auth.SignUpScreen
import com.tees.s3186984.whereabout.view.auth.SubmitSignUpScreen
import com.tees.s3186984.whereabout.viewmodel.SignUpViewModel
import com.tees.s3186984.whereabout.wutils.SIGNUP

fun NavGraphBuilder.signUpNavGraph(navController:NavController){

    val signUpViewModel = SignUpViewModel()

    navigation(startDestination = Screens.SignUp.name, SIGNUP){
        composable(Screens.SignUp.name){
            SignUpScreen(navController = navController,  signUpVM = signUpViewModel)
        }

        composable(Screens.SubmitSignUp.name){
            SubmitSignUpScreen(navController = navController,  signUpVM = signUpViewModel)
        }
    }
}