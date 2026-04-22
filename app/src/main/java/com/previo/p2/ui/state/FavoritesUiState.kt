package com.previo.p2.ui.state

import com.previo.p2.domain.model.Favorite

sealed class FavoritesUiState {
    data object Loading : FavoritesUiState()
    data class Success(val favorites: List<Favorite>) : FavoritesUiState()
    data object Empty : FavoritesUiState()
}