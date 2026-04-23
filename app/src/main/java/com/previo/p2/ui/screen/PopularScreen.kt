package com.previo.p2.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.previo.p2.ui.components.PopularRecipeCard
import com.previo.p2.ui.state.PopularUiState
import com.previo.p2.ui.viewmodel.PopularViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopularScreen(
    onMealClick: (String) -> Unit,
    viewModel: PopularViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Platillos populares", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        when (val state = uiState) {
            is PopularUiState.Loading -> LoadingSpinner()
            is PopularUiState.Error -> EmptyState(message = state.message, emoji = "⚠️")
            is PopularUiState.Success -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(state.meals) { index, meal ->
                    PopularRecipeCard(
                        position = index + 1,
                        meal = meal,
                        onClick = { onMealClick(meal.idMeal) }
                    )
                }
            }
        }
    }
}