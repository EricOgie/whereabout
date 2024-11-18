package com.tees.s3186984.whereabout.navigation

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.tees.s3186984.whereabout.ui.theme.WBackgroundGray
import com.tees.s3186984.whereabout.view.auth.LogInScreen
import com.tees.s3186984.whereabout.view.launch.OnBoardingScreen
import com.tees.s3186984.whereabout.view.launch.SplashScreen
import com.tees.s3186984.whereabout.view.main.ConnectionScreen
import com.tees.s3186984.whereabout.view.main.HistoryScreen
import com.tees.s3186984.whereabout.view.main.HomeScreen
import com.tees.s3186984.whereabout.view.main.ProfileScreen
import com.tees.s3186984.whereabout.viewmodel.HomeViewModel
import com.tees.s3186984.whereabout.viewmodel.LaunchViewModel
import com.tees.s3186984.whereabout.viewmodel.LogInViewModel


/**
 * App - The main composable function that sets up the navigation structure and system UI elements for the app.
 *
 * This function is responsible for initializing the navigation controller and handling navigation state,
 * managing system UI elements (such as the status bar and navigation bar color), and displaying the appropriate
 * screens based on the current route.
 *
 *
 * @param context The Context from the single activity where this is called
 * application-level context (such as accessing resources or initializing services).
 *
 * @see NavHost A container that sets up the navigation graph and handles the actual screen switching based on the route.
 * @see rememberSystemUiController For modifying system UI elements such as the status bar and navigation bar colors.
 */
@Composable
fun App(context: Context){

    val navController = rememberNavController()
    val systemUiController = rememberSystemUiController()
    var currentRoute by remember { mutableStateOf(Screens.Splash.name) }
    var statusColor by remember { mutableStateOf(Color.White) }
    val selectedItemIndexState = rememberSaveable { mutableStateOf(0) }

    systemUiController.setStatusBarColor(color = statusColor, darkIcons = true)


    /**
     * LaunchedEffect - Tracks navigation destination changes to update the current route.
     *
     * This effect listens for changes in the navigation destination and updates the `currentRoute` state.
     * The `currentRoute` is then used to conditionally display the bottom navigation bar and manage other UI elements.
     */
    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener {_, destination, _ ->
            currentRoute = destination.route!!
        }
    }

    /*
        * Scaffold is used here to provide a consistent layout structure for the app.
        * It handles the basic structure of the screen - bottom bar, and content area.
        *
        * The `bottomBar` parameter is conditionally shown based on the current screen route,
        * and the navigation bar color is set dynamically using the `systemUiController`.
    */
    Scaffold(
        bottomBar = {
            if (currentRoute in Screens.screenWithBottomNavBar()){
                statusColor = WBackgroundGray
                systemUiController.setNavigationBarColor(color = Color.Black)
                Box(modifier = Modifier.background(Color.Black).padding(vertical = 0.dp)){
                    BottomNavigationBar(navController, selectedItemIndexState)
                }
            }
        }

    ) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = Screens.Splash.name
        )
        {
            composable(Screens.Splash.name) {
                val launchVM = LaunchViewModel(context)
                SplashScreen(navController = navController, launchVM=launchVM)
            }
            composable(Screens.Onboarding.name) { OnBoardingScreen(navController = navController) }

            composable(Screens.LogIn.name) {
                val logInVM = LogInViewModel(context)
                LogInScreen(navController = navController, logInVM = logInVM)
            }

            composable(Screens.Home.name){
                val homeViewModel = HomeViewModel(context)
                HomeScreen(
                    navController = navController,
                    homeViewModel = homeViewModel,
                    context = context,
                    navBarIndexState = selectedItemIndexState
                )
            }
            composable(Screens.Profile.name){ ProfileScreen() }
            composable(Screens.Connection.name){ ConnectionScreen() }
            composable(Screens.History.name){ HistoryScreen() }

            signUpNavGraph(navController, context)
        }
    }

}
