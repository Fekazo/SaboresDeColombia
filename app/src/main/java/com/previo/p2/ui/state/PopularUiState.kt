package com.previo.p2.ui.state

import com.previo.p2.domain.model.MealSummary

sealed class PopularUiState {
    data object Loading : PopularUiState()
    data class Success(val meals: List<MealSummary>) : PopularUiState()
    data class Error(val message: String) : PopularUiState()
}