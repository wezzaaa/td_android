package fr.isen.sahar.thegreatestcocktailapp.data.repository

import fr.isen.sahar.thegreatestcocktailapp.data.model.CocktailCategoryDto
import fr.isen.sahar.thegreatestcocktailapp.data.model.CocktailDetailDto
import fr.isen.sahar.thegreatestcocktailapp.data.model.CocktailSummaryDto
import fr.isen.sahar.thegreatestcocktailapp.data.remote.CocktailApiService

class CocktailRepository(
    private val apiService: CocktailApiService
) {
    suspend fun getRandomCocktail(): CocktailDetailDto? {
        return apiService.getRandomCocktail().drinks?.firstOrNull()
    }

    suspend fun getCategories(): List<CocktailCategoryDto> {
        return apiService.getCategories().drinks.orEmpty()
    }

    suspend fun getDrinksByCategory(category: String): List<CocktailSummaryDto> {
        return apiService.getDrinksByCategory(category).drinks.orEmpty()
    }

    suspend fun getCocktailDetails(drinkId: String): CocktailDetailDto? {
        return apiService.getCocktailDetails(drinkId).drinks?.firstOrNull()
    }
}
