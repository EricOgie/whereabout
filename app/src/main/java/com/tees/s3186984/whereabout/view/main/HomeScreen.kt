package com.tees.s3186984.whereabout.view.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DevicesOther
import androidx.compose.material.icons.outlined.Directions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tees.s3186984.whereabout.componets.WBadge
import com.tees.s3186984.whereabout.componets.WFloatingActionButton
import com.tees.s3186984.whereabout.ui.theme.WBackgroundGray
import com.tees.s3186984.whereabout.ui.theme.WhereaboutTheme

@Composable
fun HomeScreen(navController: NavController) {

    // Parent Container
    Box(
        modifier = Modifier
            .background(color = WBackgroundGray)
            .fillMaxSize()
    ){

        // Notification Badge
        Row(
            modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(15.dp),
            horizontalArrangement = Arrangement.End
        ) {
            WBadge(
                modifier = Modifier.scale(0.9f),
                noticeCount = 4,
                glowColor = Color.White,
                iconSize = 45,
                hasBorderStroke = false
            )
        }


        // Bottom Action section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.BottomCenter)
                .background(
                    color = WBackgroundGray,
                    shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
                ),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        )
        {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 15.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Container for the first WBadge
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    WBadge(
                        modifier = Modifier.size(45.dp),
                        glowColor = Color.White,
                        icon = Icons.Outlined.Directions
                    )
                }

                WFloatingActionButton(title = "SOS" ) {  }

                // Container for the second WBadge
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    WBadge(
                        modifier = Modifier.size(45.dp),
                        glowColor = Color.White,
                        icon = Icons.Filled.DevicesOther,
                        iconTint = Color.Blue
                    )
                }
            }




        }

    } // - End of Parent Container

} // - End of Composable Screen

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    WhereaboutTheme {
        HomeScreen(navController = rememberNavController())
    }
}