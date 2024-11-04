package com.tees.s3186984.whereabout.componets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tees.s3186984.whereabout.R
import com.tees.s3186984.whereabout.navigation.Screens
import com.tees.s3186984.whereabout.ui.theme.WMeRed
import com.tees.s3186984.whereabout.wutils.LOGIN


@Composable
fun ScreenAddOn(
    navController: NavController,
    questionText: String,
    linkTitle: String,
    linDestination: String
) {

    Spacer(modifier = Modifier.height(5.dp))

    Text(
        modifier = Modifier
            .padding(0.dp),
        color = Color.Black,
        text = questionText,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily(Font(R.font.montserrat)),
        fontSize = 15.sp
    )

    Text(
        modifier = Modifier
            .padding(0.dp)
            .clickable {
                navController.navigate(linDestination) {
//                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
//                    launchSingleTop = true

                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }


            },

        color = WMeRed,
        text = linkTitle,
        textAlign = TextAlign.Center,

        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily(Font(R.font.montserrat)),
        fontSize = 13.sp
    )
}