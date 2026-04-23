package com.previo.p2.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.previo.p2.ui.components.SortBar
import com.previo.p2.ui.components.SortOrder
import com.previo.p2.ui.state.AllMealsUiState
import com.previo.p2.ui.viewmodel.AllMealsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllMealsScreen(
    onMealClick: (String) -> Unit,
    viewModel: AllMealsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val sortOrder by viewModel.sortOrder.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todos los platillos", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            when (val state = uiState) {
                is AllMealsUiState.Loading -> LoadingSpinner()
                is AllMealsUiState.Empty -> EmptyState(
                    message = "No hay platillos disponibles",
                    emoji = "🍽️"
                )
                is AllMealsUiState.Error -> EmptyState(message = state.message, emoji = "⚠️")
                is AllMealsUiState.Success -> {
                    val sorted = when (sortOrder) {
                        SortOrder.DEFAULT -> state.meals
                        SortOrder.AZ -> state.meals.sortedBy {
                            (it.translatedName ?: it.strMeal).lowercase()
                        }
                        SortOrder.ZA -> state.meals.sortedByDescending {
                            (it.translatedName ?: it.strMeal).lowercase()
                        }
                    }
                    SortBar(current = sortOrder, onSortChange = viewModel::setSortOrder)
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(sorted) { meal ->
                            RecipeCard(meal = meal, onClick = { onMealClick(meal.idMeal) })
                        }
                    }
                }
            }
        }
    }
}