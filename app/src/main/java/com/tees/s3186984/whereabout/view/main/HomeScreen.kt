package com.tees.s3186984.whereabout.view.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tees.s3186984.whereabout.ui.theme.WhereaboutTheme

@Composable
fun HomeScreen(controller: NavController) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

            Text(text = "Yippii🎉🥂🍾\n Whereabout setup Completed\n\n...Happy Developing...")
        }

    }

}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    WhereaboutTheme {
        HomeScreen(controller= rememberNavController())
    }
}