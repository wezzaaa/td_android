package fr.isen.sahar.thegreatestcocktailapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.isen.sahar.thegreatestcocktailapp.ui.AppViewModel
import fr.isen.sahar.thegreatestcocktailapp.ui.components.AppBackground
import fr.isen.sahar.thegreatestcocktailapp.ui.components.DrinkCard
import fr.isen.sahar.thegreatestcocktailapp.ui.components.ErrorView
import fr.isen.sahar.thegreatestcocktailapp.ui.components.LoadingView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrinksScreen(
    category: String,
    viewModel: AppViewModel,
    onBackClick: () -> Unit,
    onDrinkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.drinksByCategoryState.collectAsState()

    LaunchedEffect(category) {
        viewModel.loadDrinksByCategory(category)
    }

    AppBackground {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { innerPadding ->
            when {
                state.isLoading -> LoadingView(Modifier.padding(innerPadding))
                state.error != null -> ErrorView(state.error.orEmpty(), Modifier.padding(innerPadding))
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Text(
                                text = "Drinks in this category",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        items(state.data.orEmpty()) { drink ->
                            DrinkCard(drink = drink) {
                                onDrinkClick(drink.id)
                            }
                        }
                    }
                }
            }
        }
    }
}