package com.tees.s3186984.whereabout.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tees.s3186984.whereabout.ui.theme.WhereaboutTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge



@Composable
fun NotificationIconWithBadge(
    modifier: Modifier = Modifier,
    messageCount: Int,
    size: Int = 30,
    noticeIconColor: Color = Color.Black,
    badgeColor: Color = Color.Red,
    badgeTextColor: Color = Color.White,
    badgeTextSize: Int = 14,
    handleClick: () -> Unit
) {
    Box (modifier = modifier) {
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notification",
            modifier = Modifier
                .size(size.dp)
                .background(color = Color.Transparent)
                .clickable{
                    handleClick()
                },
            tint = noticeIconColor
        )

        if (messageCount > 0) {
            Badge(
                modifier = Modifier
                    .offset(x = 20.dp, y = (-2).dp),
                containerColor = badgeColor,
                contentColor = badgeTextColor
            ) {
                Text(
                    text = if (messageCount > 99) "99+" else messageCount.toString(),
                    fontSize = badgeTextSize.sp
                )
            }
        }
    }
}



@Preview
@Composable
fun Preview() {
    WhereaboutTheme {
        NotificationIconWithBadge(messageCount=5){}
    }
}