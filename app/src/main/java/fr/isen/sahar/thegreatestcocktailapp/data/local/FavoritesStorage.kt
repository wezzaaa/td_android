package fr.isen.sahar.thegreatestcocktailapp.data.local

import android.content.Context

class FavoritesStorage(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getFavoriteIds(): Set<String> {
        return sharedPreferences.getStringSet(KEY_FAVORITES, emptySet()).orEmpty()
    }

    fun isFavorite(drinkId: String): Boolean {
        return getFavoriteIds().contains(drinkId)
    }

    fun toggleFavorite(drinkId: String): Boolean {
        val updated = getFavoriteIds().toMutableSet()
        val isNowFavorite = if (updated.contains(drinkId)) {
            updated.remove(drinkId)
            false
        } else {
            updated.add(drinkId)
            true
        }
        sharedPreferences.edit().putStringSet(KEY_FAVORITES, updated).apply()
        return isNowFavorite
    }

    companion object {
        private const val PREFS_NAME = "cocktail_favorites"
        private const val KEY_FAVORITES = "favorite_ids"
    }
}
