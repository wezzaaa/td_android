package fr.isen.sahar.thegreatestcocktailapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.isen.sahar.thegreatestcocktailapp.data.local.FavoritesStorage
import fr.isen.sahar.thegreatestcocktailapp.data.model.CocktailCategoryDto
import fr.isen.sahar.thegreatestcocktailapp.data.model.CocktailDetailDto
import fr.isen.sahar.thegreatestcocktailapp.data.model.CocktailSummaryDto
import fr.isen.sahar.thegreatestcocktailapp.data.remote.ApiProvider
import fr.isen.sahar.thegreatestcocktailapp.data.repository.CocktailRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UiState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val error: String? = null
)

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CocktailRepository(ApiProvider.cocktailApiService)
    private val favoritesStorage = FavoritesStorage(application)

    private val _randomState = MutableStateFlow(UiState<CocktailDetailDto>(isLoading = true))
    val randomState: StateFlow<UiState<CocktailDetailDto>> = _randomState.asStateFlow()

    private val _categoriesState = MutableStateFlow(UiState<List<CocktailCategoryDto>>(isLoading = true))
    val categoriesState: StateFlow<UiState<List<CocktailCategoryDto>>> = _categoriesState.asStateFlow()

    private val _drinksByCategoryState = MutableStateFlow(UiState<List<CocktailSummaryDto>>(isLoading = true))
    val drinksByCategoryState: StateFlow<UiState<List<CocktailSummaryDto>>> = _drinksByCategoryState.asStateFlow()

    private val _detailState = MutableStateFlow(UiState<CocktailDetailDto>(isLoading = true))
    val detailState: StateFlow<UiState<CocktailDetailDto>> = _detailState.asStateFlow()

    private val _favoritesState = MutableStateFlow(UiState<List<CocktailDetailDto>>(isLoading = true, data = emptyList()))
    val favoritesState: StateFlow<UiState<List<CocktailDetailDto>>> = _favoritesState.asStateFlow()

    fun loadRandomCocktail() {
        viewModelScope.launch {
            _randomState.value = UiState(isLoading = true)
            _randomState.value = runCatching { repository.getRandomCocktail() }
                .fold(
                    onSuccess = { cocktail ->
                        if (cocktail != null) UiState(data = cocktail) else UiState(error = "No cocktail found")
                    },
                    onFailure = { UiState(error = it.message ?: "Failed to load random cocktail") }
                )
        }
    }

    fun loadCategories() {
        if (_categoriesState.value.data != null && _categoriesState.value.error == null) return
        viewModelScope.launch {
            _categoriesState.value = UiState(isLoading = true)
            _categoriesState.value = runCatching { repository.getCategories() }
                .fold(
                    onSuccess = { UiState(data = it) },
                    onFailure = { UiState(error = it.message ?: "Failed to load categories") }
                )
        }
    }

    fun loadDrinksByCategory(category: String) {
        viewModelScope.launch {
            _drinksByCategoryState.value = UiState(isLoading = true)
            _drinksByCategoryState.value = runCatching { repository.getDrinksByCategory(category) }
                .fold(
                    onSuccess = { UiState(data = it) },
                    onFailure = { UiState(error = it.message ?: "Failed to load drinks") }
                )
        }
    }

    fun loadCocktailDetail(drinkId: String) {
        viewModelScope.launch {
            _detailState.value = UiState(isLoading = true)
            _detailState.value = runCatching { repository.getCocktailDetails(drinkId) }
                .fold(
                    onSuccess = { cocktail ->
                        if (cocktail != null) UiState(data = cocktail) else UiState(error = "Cocktail not found")
                    },
                    onFailure = { UiState(error = it.message ?: "Failed to load cocktail details") }
                )
        }
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _favoritesState.value = UiState(isLoading = true, data = emptyList())
            _favoritesState.value = runCatching {
                val favoriteIds = favoritesStorage.getFavoriteIds().toList()
                favoriteIds.map { drinkId ->
                    async { repository.getCocktailDetails(drinkId) }
                }.awaitAll().filterNotNull()
            }.fold(
                onSuccess = { UiState(data = it) },
                onFailure = { UiState(error = it.message ?: "Failed to load favorites") }
            )
        }
    }

    fun isFavorite(drinkId: String): Boolean {
        return favoritesStorage.isFavorite(drinkId)
    }

    fun toggleFavorite(drinkId: String): Boolean {
        val result = favoritesStorage.toggleFavorite(drinkId)
        loadFavorites()
        return result
    }
}
