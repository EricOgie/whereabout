package com.tees.s3186984.whereabout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tees.s3186984.whereabout.navigation.App
import com.tees.s3186984.whereabout.ui.theme.WhereaboutTheme




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhereaboutTheme{
                App(this)
            }
        }
    }
}


