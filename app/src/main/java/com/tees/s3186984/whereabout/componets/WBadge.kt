package com.tees.s3186984.whereabout.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tees.s3186984.whereabout.ui.theme.WhereaboutTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import com.tees.s3186984.whereabout.ui.theme.WShadyAsh

@Composable
fun WBadge(
    modifier: Modifier = Modifier,
    noticeCount: Int = 0,
    glowColor: Color = WShadyAsh.copy(0.4f),
    icon: ImageVector = Icons.Outlined.Notifications,
    iconTint: Color = Color.Black.copy(0.7f),
    iconSize: Int = 40,
    hasBorderStroke: Boolean = true
){
    Box(
        modifier = modifier
            .size(60.dp)
            .background(glowColor, shape = CircleShape)
            .then(
                if(hasBorderStroke){
                    modifier.border(0.4.dp, Color.Black.copy(0.5f), shape = CircleShape)
                }else { modifier}
            )
    ){
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(iconSize.dp)
                .align(Alignment.Center)
                .padding(8.dp),
            tint = iconTint
        )

        if (noticeCount > 0){
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(30.dp)
                    .background(Color.Red, shape = CircleShape)
            ){
                Text(
                    text = noticeCount.toString(),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun Preview() {
    WhereaboutTheme {
        WBadge(noticeCount = 2)
    }
}