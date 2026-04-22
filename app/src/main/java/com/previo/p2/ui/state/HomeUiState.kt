package com.previo.p2.ui.state

import com.previo.p2.domain.model.MealSummary

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Success(val meals: List<MealSummary>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}