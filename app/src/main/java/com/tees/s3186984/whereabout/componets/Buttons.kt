package com.tees.s3186984.whereabout.componets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.tooling.preview.Preview
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
fun BackButton(handleClick: () -> Unit) {
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
fun SubmitButton(modifier: Modifier = Modifier, text: String, handleClick: () -> Unit ) {
    Button(
        onClick = handleClick,
        modifier = modifier
            .fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )
    }
}





@Preview
@Composable
fun BackButtonPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(15.dp)) {
            BackButton(handleClick = { /* Do something on click */ })
            Spacer(modifier = Modifier.height(30.dp))
            SubmitButton(text = "Agree and Continue"){}
        }

    }
}
