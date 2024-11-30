package com.tees.s3186984.whereabout

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.tees.s3186984.whereabout.navigation.App
import com.tees.s3186984.whereabout.ui.theme.WhereaboutTheme


/**
 * MainActivity - The entry point of the Whereabout app.
 *
 * This activity sets up the app's theme and launches the main composable function, `App`.
 * It extends `ComponentActivity` to integrate Jetpack Compose and acts as the host for
 * the app's entire UI, which is managed within the `App` composable.
 *
 * Inside `onCreate`, the `WhereaboutTheme` is applied to wrap the app, ensuring a consistent
 * appearance based on the app's theme design.
 *
 * @see ComponentActivity The base activity class for Compose applications.
 * @see setContent A method used to set the app's content to a composable structure.
 * @see WhereaboutTheme Applies the app's theme to all composables within this scope.
 * @see App The main composable function that defines the navigation and UI structure.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // We are applying immersive mode here
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContent {
            WhereaboutTheme{
                App(this)
            }
        }
    }
}

