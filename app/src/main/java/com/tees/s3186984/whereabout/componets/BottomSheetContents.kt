package com.tees.s3186984.whereabout.componets


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Share

import androidx.compose.material3.Card

import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tees.s3186984.whereabout.ui.theme.WhereaboutTheme


@Composable
fun QRCodeContainer(){
   Column(
       modifier = Modifier.fillMaxSize().padding(5.dp),
       verticalArrangement = Arrangement.Center,
       horizontalAlignment = Alignment.CenterHorizontally
   )
   {
       Box(
           modifier = Modifier
               .size(250.dp)
               .padding(5.dp)
               .background(color = Color.Gray),

       )

       Spacer(modifier = Modifier.height(12.dp))

       Text(
           text = "Samsung Galaxy S10, 2018",
           style = TextStyle(
               fontSize = 16.sp,
               fontWeight = FontWeight.Normal
           )
       )

       Spacer(modifier = Modifier.height(8.dp))

       Text(
           text = "Eric Ogie Aghahowa",
           style = TextStyle(
               fontSize = 20.sp,
               fontWeight = FontWeight.Bold
           )
       )
   }
}



@Composable
fun QRCodeScanner(){
    Column(
        modifier = Modifier.fillMaxSize().padding(5.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {

        Box(
            modifier = Modifier
                .size(250.dp)
                .padding(5.dp)
                .background(color = Color.Gray),
            )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Pairing As",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Samsung Galaxy S10, 2018",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Eric Ogie Aghahowa",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}


@Composable
fun LocationDetailsSheet(){
    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ){
        Text(
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black.copy(0.95f),
            text = "Your Location",
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Normal
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black.copy(0.75f),
            text = "Near 7, Somerset Street, Middlesbrough",
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable{}
                .padding(start = 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            WSmallFAB(
                modifier = Modifier.padding(0.dp),
                fabColor = Color.Blue.copy(0.7f),
                contentColor = Color.White,
                scaleFactor = 0.75f,
                icon = Icons.Filled.Share
            ) {  }

            Text(
                modifier = Modifier.padding(start = 8.dp),
                color = Color.Blue.copy(0.7f),
                text = "Share your Location",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black.copy(0.95f),
            text = "Nearby Landmarks",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            )
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp).border(0.05.dp, Color.Gray),
            colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(0.25f)),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(0.5.dp, Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Town Hall",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color.Black
                )

                HorizontalDivider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Address text
                Text(
                    text = "1, Florent Street, Inglebly Barwick, Stockton-on-Tees, North-Yorkshire",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    color = Color.Black.copy(alpha = 0.75f) // Slightly lighter color
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QRCodePreview() {
    WhereaboutTheme {
        LocationDetailsSheet()
    }
}
