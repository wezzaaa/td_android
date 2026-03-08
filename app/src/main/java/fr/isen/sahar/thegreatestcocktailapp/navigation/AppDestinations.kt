package fr.isen.sahar.thegreatestcocktailapp.navigation

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class AppDestination(val route: String) {
    data object Random : AppDestination("random")
    data object Categories : AppDestination("categories")
    data object Favorites : AppDestination("favorites")
    data object DrinksByCategory : AppDestination("drinks/{category}") {
        fun createRoute(category: String): String {
            val encoded = URLEncoder.encode(category, StandardCharsets.UTF_8.toString())
            return "drinks/$encoded"
        }
    }
    data object Detail : AppDestination("detail/{drinkId}") {
        fun createRoute(drinkId: String): String {
            val encoded = URLEncoder.encode(drinkId, StandardCharsets.UTF_8.toString())
            return "detail/$encoded"
        }
    }
}
