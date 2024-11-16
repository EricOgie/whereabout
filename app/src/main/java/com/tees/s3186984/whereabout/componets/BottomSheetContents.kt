package com.tees.s3186984.whereabout.componets


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tees.s3186984.whereabout.ui.theme.WhereaboutTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton

/**
 * Displays a container that shows a QR code with additional details.
 * Includes an image box, followed by device details and user information.
 */
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

/**
 * Displays a QR code scanner interface with device and user information.
 * Includes a box for the QR code scanner, a title, and user details.
 */
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

/**
 * Displays a sheet containing location details.
 * Shows a title, address information, and other components.
 */
@Composable
fun LocationDetailsSheet(){
    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ){
        Text(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp),
            color = Color.Black.copy(0.95f),
            text = "Your Location",
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Normal
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

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
                scaleFactor = 0.65f,
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

            colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(0.09f)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
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
                    text = "1, Florent Street, Inglebly Barwick, Stockton-on-Tees",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    color = Color.Black.copy(alpha = 0.75f)
                )
            }
        }
    }
}

/**
 * Displays a sheet containing real-time location sharing information.
 * Shows a share time duration, and devices to share location with.
 */
@Composable
fun ShareLocationSheet() {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight().padding(10.dp),
        ){
            // ----------- Sheet Title ------------------------- //
            Text(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp),
                color = Color.Black.copy(0.95f),
                text = "Share real-time location",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal)
            )

            Spacer(modifier = Modifier.height(15.dp))
            // --------------- Share Time Section --------------  //
            Row(
                modifier = Modifier.fillMaxWidth().padding(0.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    WRadioButton(
                        modifier = Modifier.padding(0.dp),
                        selected = true,
                        onSelect = {},
                        size = 40f
                    )

                    Column{
                        Text(
                            text = "For 2 hours", color = Color.Black,
                            fontSize = 14.sp

                        )
                        Text(
                            text = "Until 13.15",
                            color = Color.Gray,
                            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal),
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.End) {

                    WSmallFAB(
                        fabColor = Color.Blue.copy(0.8f),
                        contentColor = Color.White,
                        scaleFactor = 0.55f,
                        icon = Icons.Filled.Add
                    ) {  }

                    WSmallFAB(
                        fabColor = Color.Red.copy(0.8f),
                        contentColor = Color.White,
                        scaleFactor = 0.55f,
                        icon = Icons.Filled.Remove
                    ) {  }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(0.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                WRadioButton(
                    modifier = Modifier.padding(0.dp),
                    selected = false,
                    onSelect = {},
                    size = 40f
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Until you turn sharing off",
                    color = Color.Black,
                    fontSize = 14.sp
                )
            }
        }


        HorizontalDivider(
            color = Color.Gray.copy(0.8f),
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 5.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth()
                .wrapContentHeight().padding(10.dp),
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.padding(start = 8.dp),
                color = Color.Black.copy(0.95f),
                text = "Your Connection",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // TODO -- REPLACE dummy data with lazyColumn --//
                WSelectableCircularImage(
                    id = "1twhwt7",
                    title = "Mom's Phone"
                ) { }

                WSelectableCircularImage(
                    id = "1twhwt7",
                    title = "James's Phone"
                ) { }

                WSelectableCircularImage(
                    id = "1twhwt7",
                    title = "Grace",
                    isSelected = true
                ) { }

            }
        }

        HorizontalDivider(
            color = Color.Gray.copy(0.8f),
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 5.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(end = 8.dp),
            horizontalArrangement = Arrangement.Center
        ){
            TextButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    color = Color.Blue.copy(0.75f),
                    text = "Share",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal)
                )
            }
        }


    }

}


@Preview(showBackground = true)
@Composable
fun QRCodePreview() {
    WhereaboutTheme {
        ShareLocationSheet()
    }
}






