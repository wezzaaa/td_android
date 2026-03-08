package fr.isen.sahar.thegreatestcocktailapp

import android.os.Bundle
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fr.isen.sahar.thegreatestcocktailapp.navigation.AppDestination
import fr.isen.sahar.thegreatestcocktailapp.screens.CategoriesScreen
import fr.isen.sahar.thegreatestcocktailapp.screens.CocktailBottomBar
import fr.isen.sahar.thegreatestcocktailapp.screens.DetailCocktailScreen
import fr.isen.sahar.thegreatestcocktailapp.screens.DrinksScreen
import fr.isen.sahar.thegreatestcocktailapp.screens.FavoritesScreen
import fr.isen.sahar.thegreatestcocktailapp.screens.RandomCocktailScreen
import fr.isen.sahar.thegreatestcocktailapp.ui.AppViewModel
import fr.isen.sahar.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme

data class TabBarItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val viewModel: AppViewModel = viewModel()

            val randomItem = TabBarItem(
                stringResource(R.string.tab_item_random),
                AppDestination.Random.route,
                Icons.Filled.Home,
                Icons.Outlined.Home
            )
            val categoryItem = TabBarItem(
                stringResource(R.string.tab_item_category),
                AppDestination.Categories.route,
                Icons.Filled.Menu,
                Icons.Outlined.Menu
            )
            val favoriteItem = TabBarItem(
                stringResource(R.string.tab_item_favorite),
                AppDestination.Favorites.route,
                Icons.Filled.Favorite,
                Icons.Outlined.Favorite
            )
            val tabItems = listOf(randomItem, categoryItem, favoriteItem)

            TheGreatestCocktailAppTheme {
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = backStackEntry?.destination
                val showBottomBar = currentDestination?.route in setOf(
                    AppDestination.Random.route,
                    AppDestination.Categories.route,
                    AppDestination.Favorites.route
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar) {
                            CocktailBottomBar(
                                items = tabItems,
                                navController = navController,
                                currentRoute = currentDestination?.route
                            )
                        }
                    }
                ) { innerPadding ->
                    CocktailNavGraph(
                        navController = navController,
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@androidx.compose.runtime.Composable
private fun CocktailNavGraph(
    navController: NavHostController,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.Random.route,
        modifier = modifier
    ) {
        composable(AppDestination.Random.route) {
            RandomCocktailScreen(
                viewModel = viewModel,
                onDrinkClick = { drinkId ->
                    navController.navigate(AppDestination.Detail.createRoute(drinkId))
                }
            )
        }
        composable(AppDestination.Categories.route) {
            CategoriesScreen(
                viewModel = viewModel,
                onCategoryClick = { category ->
                    navController.navigate(AppDestination.DrinksByCategory.createRoute(category))
                }
            )
        }
        composable(AppDestination.Favorites.route) {
            FavoritesScreen(
                viewModel = viewModel,
                onDrinkClick = { drinkId ->
                    navController.navigate(AppDestination.Detail.createRoute(drinkId))
                }
            )
        }
        composable(AppDestination.DrinksByCategory.route) { backStackEntry ->
            val category = URLDecoder.decode(
                backStackEntry.arguments?.getString("category").orEmpty(),
                StandardCharsets.UTF_8.toString()
            )
            DrinksScreen(
                category = category,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onDrinkClick = { drinkId ->
                    navController.navigate(AppDestination.Detail.createRoute(drinkId))
                }
            )
        }
        composable(AppDestination.Detail.route) { backStackEntry ->
            val drinkId = URLDecoder.decode(
                backStackEntry.arguments?.getString("drinkId").orEmpty(),
                StandardCharsets.UTF_8.toString()
            )
            DetailCocktailScreen(
                drinkId = drinkId,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
