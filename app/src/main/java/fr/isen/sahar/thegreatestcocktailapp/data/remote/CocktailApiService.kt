package fr.isen.sahar.thegreatestcocktailapp.data.remote

import fr.isen.sahar.thegreatestcocktailapp.data.model.CocktailCategoryResponse
import fr.isen.sahar.thegreatestcocktailapp.data.model.CocktailDetailResponse
import fr.isen.sahar.thegreatestcocktailapp.data.model.CocktailSummaryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailApiService {
    @GET("random.php")
    suspend fun getRandomCocktail(): CocktailDetailResponse

    @GET("list.php?c=list")
    suspend fun getCategories(): CocktailCategoryResponse

    @GET("filter.php")
    suspend fun getDrinksByCategory(
        @Query("c") category: String
    ): CocktailSummaryResponse

    @GET("lookup.php")
    suspend fun getCocktailDetails(
        @Query("i") drinkId: String
    ): CocktailDetailResponse
}
