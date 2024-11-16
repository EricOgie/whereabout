package com.tees.s3186984.whereabout.componets


import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Sos
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
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
    buttonColor: Color = Color.Black,
    textColor: Color = Color.White,
    loadingState: MutableState<Boolean> = mutableStateOf(false),
    handleClick: () -> Unit )
{

    Button(
        onClick = handleClick,
        modifier = modifier
            .fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
    ) {
        if (loadingState.value){
            WLoadingBar()
        }else{
            Text(
                text = text,
                fontSize = 18.sp,
                color = textColor,
                style = MaterialTheme.typography.titleMedium
            )
        }

    }
}




@Composable
fun WLargeFAB(
    shape: Shape = CircleShape,
    scaleFactor: Float = 0.7f,
    fabColor: Color = Color.Red,
    contentColor: Color = Color.White,
    icon: ImageVector = Icons.Filled.Sos,
    handleClick: () -> Unit
) {
    LargeFloatingActionButton(
        modifier = Modifier.scale(scaleFactor),
        onClick = { handleClick() },
        containerColor = fabColor,
        contentColor = contentColor,
        shape = shape,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(30.dp)
        )
    }

}


@Composable
fun WSmallFAB(
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    scaleFactor: Float = 0.85f,
    fabColor: Color = Color.White,
    contentColor: Color = Color.Black,
    icon: ImageVector = Icons.Filled.QrCode,
    handleClick: () -> Unit
) {
    SmallFloatingActionButton(
        modifier = modifier.scale(scaleFactor),
        onClick = { handleClick() },
        containerColor = fabColor,
        contentColor = contentColor,
        shape = shape,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
        )
    }
}


@Composable
fun WRadioButton(
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: Color = Color.Blue.copy(0.7f),
    unselectedColor: Color = Color.Gray,
    size: Float = 24f,
    strokeWidth: Float = 2f
)
{
    Canvas(
        modifier = modifier
            .size(size.dp)
            .clickable(onClick = onSelect)
    ) {
        drawCircle(
            color = if (selected) selectedColor else unselectedColor,
            radius = size / 2,
            style = Stroke(width = strokeWidth.dp.toPx())
        )

        if (selected) {
            drawCircle(
                color = selectedColor,
                radius = (size / 2) - strokeWidth.dp.toPx() * 1.4f
            )
        }
    }
}



