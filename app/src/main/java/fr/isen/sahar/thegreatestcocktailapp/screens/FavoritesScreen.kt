package fr.isen.sahar.thegreatestcocktailapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.isen.sahar.thegreatestcocktailapp.ui.AppViewModel
import fr.isen.sahar.thegreatestcocktailapp.ui.components.AppBackground
import fr.isen.sahar.thegreatestcocktailapp.ui.components.EmptyFavoritesView
import fr.isen.sahar.thegreatestcocktailapp.ui.components.ErrorView
import fr.isen.sahar.thegreatestcocktailapp.ui.components.FavoriteCard
import fr.isen.sahar.thegreatestcocktailapp.ui.components.LoadingView

@Composable
fun FavoritesScreen(
    viewModel: AppViewModel,
    onDrinkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.favoritesState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    AppBackground {
        when {
            state.isLoading -> LoadingView(modifier)
            state.error != null -> ErrorView(state.error.orEmpty(), modifier)
            state.data.isNullOrEmpty() -> EmptyFavoritesView(modifier)
            else -> {
                LazyColumn(
                    modifier = modifier.fillMaxSize(),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = "Your favorite cocktails",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    item {
                        Text(
                            text = "All drinks you save from the detail screen are stored locally with SharedPreferences.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                        )
                    }
                    items(state.data.orEmpty()) { cocktail ->
                        FavoriteCard(cocktail = cocktail) {
                            onDrinkClick(cocktail.id)
                        }
                    }
                }
            }
        }
    }
}