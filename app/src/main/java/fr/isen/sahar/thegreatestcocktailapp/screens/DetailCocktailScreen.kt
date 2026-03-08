package fr.isen.sahar.thegreatestcocktailapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import fr.isen.sahar.thegreatestcocktailapp.R
import fr.isen.sahar.thegreatestcocktailapp.ui.AppViewModel
import fr.isen.sahar.thegreatestcocktailapp.ui.components.AppBackground
import fr.isen.sahar.thegreatestcocktailapp.ui.components.ErrorView
import fr.isen.sahar.thegreatestcocktailapp.ui.components.LoadingView
import fr.isen.sahar.thegreatestcocktailapp.ui.components.SectionCard
import fr.isen.sahar.thegreatestcocktailapp.ui.theme.AccentGold
import fr.isen.sahar.thegreatestcocktailapp.ui.theme.AccentRose
import fr.isen.sahar.thegreatestcocktailapp.ui.theme.SurfaceGlass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailCocktailScreen(
    drinkId: String,
    viewModel: AppViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.detailState.collectAsState()
    var isFavorite by remember(drinkId) { mutableStateOf(viewModel.isFavorite(drinkId)) }

    LaunchedEffect(drinkId) {
        viewModel.loadCocktailDetail(drinkId)
        isFavorite = viewModel.isFavorite(drinkId)
    }

    AppBackground {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.detail_title),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                isFavorite = viewModel.toggleFavorite(drinkId)
                            }
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (isFavorite) AccentRose else MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            when {
                state.isLoading -> LoadingView(Modifier.padding(innerPadding))
                state.error != null -> ErrorView(state.error.orEmpty(), Modifier.padding(innerPadding))
                state.data != null -> {
                    val cocktail = state.data!!
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        item {
                            AsyncImage(
                                model = cocktail.imageUrl,
                                contentDescription = cocktail.name,
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        item {
                            Text(
                                text = cocktail.name,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        item {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                AssistChip(
                                    onClick = {},
                                    label = { Text(cocktail.category.orEmpty()) },
                                    colors = AssistChipDefaults.assistChipColors(containerColor = SurfaceGlass)
                                )
                                AssistChip(
                                    onClick = {},
                                    label = { Text(cocktail.alcoholic.orEmpty()) },
                                    colors = AssistChipDefaults.assistChipColors(containerColor = SurfaceGlass)
                                )
                                AssistChip(
                                    onClick = {},
                                    label = { Text("${stringResource(R.string.glass_title)}: ${cocktail.glass.orEmpty()}") },
                                    colors = AssistChipDefaults.assistChipColors(containerColor = SurfaceGlass, labelColor = AccentGold)
                                )
                            }
                        }
                        item {
                            SectionCard(title = stringResource(R.string.ingredients_title)) {
                                cocktail.ingredientsWithMeasures().forEach { ingredient ->
                                    Text(
                                        text = ingredient,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                        item {
                            SectionCard(title = stringResource(R.string.instructions_title)) {
                                Text(
                                    text = cocktail.instructions.orEmpty(),
                                    style = MaterialTheme.typography.bodyLarge,
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