package com.lab.saboresdecolombia.feature.allrecipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab.saboresdecolombia.core.domain.model.Recipe
import com.lab.saboresdecolombia.core.domain.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AllRecipesUiState(
    val recipes: List<Recipe> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val filteredRecipes: List<Recipe> = emptyList(),
    val sortAscending: Boolean = true
)

@HiltViewModel
class AllRecipesViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AllRecipesUiState())
    val uiState: StateFlow<AllRecipesUiState> = _uiState.asStateFlow()

    init {
        loadAllRecipes()
    }

    private fun loadAllRecipes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            recipeRepository.getAllRecipes()
                .onSuccess { recipes ->
                    val sorted = sortRecipes(recipes, ascending = true)
                    _uiState.update {
                        it.copy(
                            recipes = sorted,
                            filteredRecipes = sorted,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(error = error.message, isLoading = false) }
                }
        }
    }

    fun toggleSort() {
        _uiState.update { state ->
            val ascending = !state.sortAscending
            val sorted = sortRecipes(state.filteredRecipes, ascending)
            state.copy(sortAscending = ascending, filteredRecipes = sorted)
        }
    }

    fun onSearchQueryChange(query: String) {
        val state = _uiState.value
        val allRecipes = state.recipes
        val filtered = if (query.isBlank()) {
            allRecipes
        } else {
            allRecipes.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.category.contains(query, ignoreCase = true) ||
                        it.area.contains(query, ignoreCase = true)
            }
        }
        val sorted = sortRecipes(filtered, state.sortAscending)
        _uiState.update { it.copy(searchQuery = query, filteredRecipes = sorted) }
    }

    private fun sortRecipes(list: List<Recipe>, ascending: Boolean): List<Recipe> {
        return if (ascending) {
            list.sortedBy { it.name.lowercase() }
        } else {
            list.sortedByDescending { it.name.lowercase() }
        }
    }
}
