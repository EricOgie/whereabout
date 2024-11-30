package com.tees.s3186984.whereabout.view.launch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tees.s3186984.whereabout.ui.theme.WBackgroundGray
import com.tees.s3186984.whereabout.ui.theme.WBlue5
import com.tees.s3186984.whereabout.wutils.CANCEL
import com.tees.s3186984.whereabout.wutils.CRASH_ERROR

@Preview
@Composable
fun ErrorScreen(){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = WBackgroundGray
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            Icon(
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = null,
                modifier = Modifier.size(75.dp),
                tint = Color.Red
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = CRASH_ERROR,
                color = Color.DarkGray,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}