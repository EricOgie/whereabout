package com.tees.s3186984.whereabout.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.tees.s3186984.whereabout.view.auth.SignUpScreen
import com.tees.s3186984.whereabout.view.auth.SubmitSignUpScreen
import com.tees.s3186984.whereabout.viewmodel.SignUpViewModel
import com.tees.s3186984.whereabout.wutils.SIGNUP

/**
 * signUpNavGraph - Sets up the navigation graph for the sign-up flow.
 *
 * This function creates a navigation graph within the main app's navigation structure,
 * managing the routes for the sign-up process. It defines two composable destinations:
 * `SignUpScreen` and `SubmitSignUpScreen`, both sharing a single instance of `SignUpViewModel`
 * for consistent state management across the sign-up flow.
 *
 * @param navController The NavController responsible for managing navigation between screens.
 *
 * The navigation graph includes:
 * - `Screens.SignUp`: The initial sign-up screen where users can begin the sign-up process.
 * - `Screens.SubmitSignUp`: The final screen in the sign-up flow, allowing users to submit
 *   their details to complete the process.
 *
 * @see NavGraphBuilder For creating nested navigation graphs.
 * @see composable For defining individual composable destinations within the navigation graph.
 * @see navigation For setting up a nested navigation flow within the main graph.
 */
fun NavGraphBuilder.signUpNavGraph(navController:NavController){

    // ViewModel for managing state and logic in both screens of the sign-up flow
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