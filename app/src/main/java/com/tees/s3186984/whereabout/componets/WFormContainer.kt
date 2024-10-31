package com.tees.s3186984.whereabout.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
