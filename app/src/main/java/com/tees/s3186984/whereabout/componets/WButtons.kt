package com.tees.s3186984.whereabout.componets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CrisisAlert
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * A composable function that displays a back button.
 *
 * This button is designed to allow users to navigate back in the application.
 * It consists of a circular surface that responds to click events,
 * containing a back arrow icon.
 *
 * @param handleClick A lambda function that is executed when the button is clicked.
 */
@Composable
fun WBackButton(handleClick: () -> Unit) {
    Surface(modifier = Modifier
            .size(38.dp)
            .clickable(onClick = handleClick),
        shape = CircleShape,
        color = Color.White) {

        Box(contentAlignment = Alignment.Center) {
            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }
    }
}



/**
 * A custom rectangular button composable typically for submitting forms.
 *
 * This button is intended for submitting user input,
 * It is styled with a rounded shape and fills the maximum width of its parent.
 *
 * @param modifier Optional modifier to customize the button's appearance.
 * @param text The text to be displayed on the button.
 * @param handleClick A lambda function that is executed when the button is clicked.
 */
@Composable
fun WSubmitButton(
    modifier: Modifier = Modifier,
    text: String,
    loadingState: MutableState<Boolean> = mutableStateOf(false),
    handleClick: () -> Unit )
{

    Button(
        onClick = handleClick,
        modifier = modifier
            .fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
    ) {
        if (loadingState.value){
            WLoadingBar()
        }else{
            Text(
                text = text,
                fontSize = 18.sp,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }

    }
}




@Composable
fun WFloatingActionButton(
    modifier: Modifier = Modifier,
    title: String? = null,
    icon: ImageVector = Icons.Outlined.CrisisAlert,
    handleClick: () -> Unit
) {
    Box(
        modifier = modifier.wrapContentSize(),
        contentAlignment = Alignment.Center
    ) {
        FloatingActionButton(
            modifier = Modifier
                .size(70.dp),
            containerColor = Color.Red,
            onClick = { handleClick() }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Icon displayed on top
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )

                // Text displayed below the icon
                if (title != null) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}




