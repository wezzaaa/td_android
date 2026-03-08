package fr.isen.sahar.thegreatestcocktailapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import fr.isen.sahar.thegreatestcocktailapp.ui.AppViewModel
import fr.isen.sahar.thegreatestcocktailapp.ui.components.AppBackground
import fr.isen.sahar.thegreatestcocktailapp.ui.components.ErrorView
import fr.isen.sahar.thegreatestcocktailapp.ui.components.LoadingView
import fr.isen.sahar.thegreatestcocktailapp.ui.components.SectionCard
import fr.isen.sahar.thegreatestcocktailapp.ui.theme.AccentGold
import fr.isen.sahar.thegreatestcocktailapp.ui.theme.SurfaceGlass

@Composable
fun RandomCocktailScreen(
    viewModel: AppViewModel,
    onDrinkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.randomState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRandomCocktail()
    }

    AppBackground {
        when {
            state.isLoading -> LoadingView(modifier)
            state.error != null -> ErrorView(state.error.orEmpty(), modifier)
            state.data != null -> {
                val cocktail = state.data!!
                LazyColumn(
                    modifier = modifier.fillMaxSize(),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    item {
                        Text(
                            text = "Discover a surprise cocktail",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    item {
                        Text(
                            text = "Refresh the screen to instantly explore a new recipe from TheCocktailDB.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                        )
                    }
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceGlass),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(30.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                AsyncImage(
                                    model = cocktail.imageUrl,
                                    contentDescription = cocktail.name,
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                                )
                                Text(
                                    text = cocktail.name,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = cocktail.category.orEmpty(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = AccentGold
                                )
                                Text(
                                    text = cocktail.instructions.orEmpty(),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.86f)
                                )
                                Button(
                                    onClick = { onDrinkClick(cocktail.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = AccentGold)
                                ) {
                                    Text(text = "Open details", color = MaterialTheme.colorScheme.background)
                                }
                            }
                        }
                    }
                    item {
                        Button(
                            onClick = { viewModel.loadRandomCocktail() },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentGold),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                            Text(text = " Another random cocktail")
                        }
                    }
                    item {
                        SectionCard(title = "Ingredients") {
                            cocktail.ingredientsWithMeasures().forEach { ingredient ->
                                Text(
                                    text = ingredient,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
