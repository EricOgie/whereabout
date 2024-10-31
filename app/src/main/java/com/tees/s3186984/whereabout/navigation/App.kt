package com.tees.s3186984.whereabout.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import com.tees.s3186984.whereabout.view.main.HomeScreen


@Composable
fun App(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.Home.name) {
        composable(Screens.Home.name){ HomeScreen(controller = navController) }
    }
}

