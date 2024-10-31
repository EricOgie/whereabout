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


/**
 * App - The main composable function that sets up the navigation structure for the app.
 *
 * This function initializes the navigation controller and configures system UI elements,
 * such as the status bar color. It defines the navigation graph, starting with the splash screen
 * and providing routes to other screens.
 *
 * @param context The Context from the application, needed for view models that require
 * application-level context.
 *
 * The navigation structure includes:
 * - `Screens.Splash`: The splash screen, initialized with a `LaunchViewModel`.
 * - `Screens.Onboarding`: The onboarding screen for new users.
 * - `Screens.LogIn`: The login screen, initialized with a `LogInViewModel`.
 * - `Screens.Home`: The home screen, which serves as the main user interface.
 *  - TODO - Add more screens to this list as the application grows
 *
 * @see NavController Used to control and manage navigation between screens in the app.
 * @see NavHost Sets up the navigation graph with defined composable destinations.
 * @see rememberSystemUiController For modifying system UI elements, such as status bar color.
 * @see signUpNavGraph Adds a nested navigation graph for the sign-up flow.
 */
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

