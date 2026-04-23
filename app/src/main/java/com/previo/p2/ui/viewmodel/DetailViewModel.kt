package com.previo.p2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.previo.p2.domain.model.Favorite
import com.previo.p2.domain.repository.FavoriteRepository
import com.previo.p2.domain.repository.MealRepository
import com.previo.p2.domain.repository.NutritionRepository
import com.previo.p2.domain.util.Result
import com.previo.p2.ui.state.DetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val mealRepository: MealRepository,
    private val favoriteRepository: FavoriteRepository,
    private val nutritionRepository: NutritionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadMeal(id: String) {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            when (val result = mealRepository.getMealDetail(id)) {
                is Result.Success -> {
                    val meal = result.data
                    val isFavorite = favoriteRepository.isFavorite(id)
                    _uiState.value = DetailUiState.Success(meal = meal, isFavorite = isFavorite)
                    loadNutrition(meal.translatedName ?: meal.strMeal)
                }
                is Result.Error -> {
                    _uiState.value = DetailUiState.Error(result.message ?: "Error al cargar detalle")
                }
                is Result.Loading -> Unit
            }
        }
    }

    private fun loadNutrition(query: String) {
        val current = _uiState.value
        if (current !is DetailUiState.Success) return
        viewModelScope.launch {
            _uiState.value = current.copy(nutritionLoading = true)
            when (val result = nutritionRepository.getNutrition(query)) {
                is Result.Success -> {
                    val updated = _uiState.value
                    if (updated is DetailUiState.Success) {
                        _uiState.value = updated.copy(nutrition = result.data, nutritionLoading = false)
                    }
                }
                is Result.Error -> {
                    val updated = _uiState.value
                    if (updated is DetailUiState.Success) {
                        _uiState.value = updated.copy(nutritionLoading = false)
                    }
                }
                is Result.Loading -> Unit
            }
        }
    }

    fun toggleFavorite() {
        val current = _uiState.value
        if (current !is DetailUiState.Success) return
        viewModelScope.launch {
            val meal = current.meal
            if (current.isFavorite) {
                favoriteRepository.removeFavorite(meal.idMeal)
            } else {
                favoriteRepository.addFavorite(
                    Favorite(
                        idMeal = meal.idMeal,
                        strMeal = meal.strMeal,
                        strMealThumb = meal.strMealThumb,
                        strArea = meal.strArea,
                        translatedName = meal.translatedName
                    )
                )
            }
            _uiState.value = current.copy(isFavorite = !current.isFavorite)
        }
    }
}