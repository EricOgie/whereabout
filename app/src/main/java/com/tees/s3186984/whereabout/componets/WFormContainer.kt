package com.tees.s3186984.whereabout.componets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tees.s3186984.whereabout.R
import com.tees.s3186984.whereabout.ui.theme.WSelectedStateColor

/**
 * A form container for grouping form-related components.
 *
 * This composable creates a styled container. It provides a white background with rounded
 * corners and adds padding for better spacing. You can customize the container by
 * passing a modifier.
 *
 * @param modifier Optional modifier for styling the container.
 * @param content A lambda function that allows adding composables within the column scope.
 *      The content can be any composables that represent form fields or other UI elements.
 */
@Composable
fun WFormContainer(modifier: Modifier = Modifier, content:  @Composable (ColumnScope.() -> Unit)){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color.White, shape = RoundedCornerShape(15.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.Start
    ) {
        content()
    }
}


@Composable
fun WSelectableCircularImage(
    id: String?,
    title: String,
    isSelected: Boolean = false,
    handleClick: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable{
                handleClick(id?: "")
            }
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            // Circular Image from resources
            Image(
                painter = painterResource(id = R.drawable.phone),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface),
                contentScale = ContentScale.Fit
            )

            // Overlay to indicate selected state
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            color = WSelectedStateColor,
                            shape = CircleShape
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Image Title
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
