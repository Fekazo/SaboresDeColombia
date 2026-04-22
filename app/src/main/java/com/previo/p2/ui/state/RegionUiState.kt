package com.previo.p2.ui.state

import com.previo.p2.domain.model.MealSummary

sealed class RegionUiState {
    data object Loading : RegionUiState()
    data class Success(val meals: List<MealSummary>) : RegionUiState()
    data class Error(val message: String) : RegionUiState()
    data object Empty : RegionUiState()
}