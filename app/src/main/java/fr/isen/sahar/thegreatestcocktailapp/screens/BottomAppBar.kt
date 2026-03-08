package fr.isen.sahar.thegreatestcocktailapp.screens

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import fr.isen.sahar.thegreatestcocktailapp.TabBarItem

@Composable
fun CocktailBottomBar(
    items: List<TabBarItem>,
    navController: NavHostController,
    currentRoute: String?
) {
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    TabBarIcon(
                        currentRoute == item.route,
                        item.selectedIcon,
                        item.unselectedIcon,
                        item.title
                    )
                },
                label = { Text(item.title) }
            )
        }
    }
}

@Composable
fun TabBarIcon(
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    title: String
) {
    Icon(
        if (isSelected) selectedIcon else unselectedIcon,
        contentDescription = title
    )
}