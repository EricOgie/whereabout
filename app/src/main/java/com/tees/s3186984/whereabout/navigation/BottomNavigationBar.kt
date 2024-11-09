package com.tees.s3186984.whereabout.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DevicesOther
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.DevicesOther
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tees.s3186984.whereabout.ui.theme.WhereaboutTheme

/**
 * Composable function to display a bottom navigation bar with customizable navigation items.
 * Each item updates the current selection state and navigates to the corresponding screen when clicked.
 *
 * @param navController The NavHostController used for navigating between different screens.
 */
@Composable
fun BottomNavigationBar(navController: NavHostController, selectedItemIndexState: MutableState<Int>) {
    NavigationBar(containerColor = Color.Black) {
        navItems.forEachIndexed { index, item ->
            var isSelected = selectedItemIndexState.value == index
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    // Update the selected item index and navigate to the chosen scree
                    selectedItemIndexState.value = index
                    // Navigate accordingly
                    navController.navigate(item.title){
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true // Prevent duplicate items in the back stack
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selectedItemIndexState.value == index) item.onSelectedIcon else item.defaultIcon,
                        contentDescription = item.title,
                        tint = if (isSelected) Color.White else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        color = if (isSelected) Color.White else Color.Gray,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Transparent,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}


/**
 * Data class representing a navigation item for the bottom navigation bar.
 *
 * @param title The title of the navigation item.
 * @param onSelectedIcon The icon displayed when the item is selected.
 * @param defaultIcon The icon displayed when the item is not selected.
 */
data class NavigationItem(
    val title: String,
    val onSelectedIcon: ImageVector,
    val defaultIcon: ImageVector,
)

// List of navigation items with specific icons for each screen
val navItems = listOf<NavigationItem>(

    NavigationItem(
        title = Screens.Home.name,
        onSelectedIcon = Icons.Filled.Home,
        defaultIcon = Icons.Outlined.Home,
    ),

    NavigationItem(
        title = Screens.Connection.name,
        onSelectedIcon = Icons.Filled.DevicesOther,
        defaultIcon = Icons.Outlined.DevicesOther,
    ),

    NavigationItem(
        title = Screens.Profile.name,
        onSelectedIcon = Icons.Filled.Person,
        defaultIcon = Icons.Outlined.Person,
    )

)

@Preview
@Composable
fun NavBarPreview() {
    WhereaboutTheme {

    }
}

