package com.previo.p2.ui.state

import com.previo.p2.domain.model.Meal
import com.previo.p2.domain.model.Nutrition

sealed class DetailUiState {
    data object Loading : DetailUiState()
    data class Success(
        val meal: Meal,
        val isFavorite: Boolean,
        val nutrition: Nutrition? = null,
        val nutritionLoading: Boolean = false
    ) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}