package com.previo.p2.ui.state

import com.previo.p2.domain.model.MealSummary

sealed class SearchUiState {
    data object Idle : SearchUiState()
    data object Loading : SearchUiState()
    data class Success(val meals: List<MealSummary>) : SearchUiState()
    data class Empty(val query: String) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}