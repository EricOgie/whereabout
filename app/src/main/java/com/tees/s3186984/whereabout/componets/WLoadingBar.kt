package com.tees.s3186984.whereabout.componets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import com.airbnb.lottie.compose.LottieAnimation
import com.tees.s3186984.whereabout.R
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition



/**
 * WLoadingBar - A custom loading animation that uses Lottie.
 *
 * This composable displays an animated loading bar that loops indefinitely.
 * The animation is loaded from a raw Lottie resource file, allowing for a
 * more visually engaging loading indicator.
 *
 * @see rememberLottieComposition Caches the Lottie composition to optimize
 * rendering performance, ensuring the animation is not reloaded unnecessarily.
 * @see LottieAnimation Displays the Lottie animation and configures it to
 * loop indefinitely using LottieConstants.IterateForever.
 */
@Composable
fun WLoadingBar(scaleFactor: Float = 2f){

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.loaderbar)
    )

    LottieAnimation(
        modifier = Modifier.scale(scaleFactor),
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
}