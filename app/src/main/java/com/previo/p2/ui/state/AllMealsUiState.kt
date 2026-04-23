package com.previo.p2.ui.state

import com.previo.p2.domain.model.MealSummary

sealed class AllMealsUiState {
    data object Loading : AllMealsUiState()
    data class Success(val meals: List<MealSummary>) : AllMealsUiState()
    data class Error(val message: String) : AllMealsUiState()
    data object Empty : AllMealsUiState()
}