package com.previo.p2.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.previo.p2.ui.components.EmptyState
import com.previo.p2.ui.components.LoadingSpinner
import com.previo.p2.ui.components.RecipeCard
import com.previo.p2.domain.model.MealSummary
import com.previo.p2.ui.state.FavoritesUiState
import com.previo.p2.ui.viewmodel.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onMealClick: (String) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis favoritos", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        when (val state = uiState) {
            is FavoritesUiState.Loading -> LoadingSpinner()
            is FavoritesUiState.Empty -> EmptyState(
                message = "Aún no tienes recetas guardadas",
                emoji = "❤️"
            )
            is FavoritesUiState.Success -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.favorites) { favorite ->
                    RecipeCard(
                        meal = MealSummary(
                            idMeal = favorite.idMeal,
                            strMeal = favorite.strMeal,
                            strMealThumb = favorite.strMealThumb
                        ),
                        onClick = { onMealClick(favorite.idMeal) }
                    )
                }
            }
        }
    }
}