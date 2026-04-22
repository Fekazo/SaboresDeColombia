package com.previo.p2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.previo.p2.domain.repository.MealRepository
import com.previo.p2.domain.util.Result
import com.previo.p2.ui.state.RegionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegionViewModel @Inject constructor(
    private val mealRepository: MealRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegionUiState>(RegionUiState.Loading)
    val uiState: StateFlow<RegionUiState> = _uiState.asStateFlow()

    fun loadMealsByArea(area: String) {
        viewModelScope.launch {
            _uiState.value = RegionUiState.Loading
            when (val result = mealRepository.getMealsByArea(area)) {
                is Result.Success -> {
                    _uiState.value = if (result.data.isEmpty()) {
                        RegionUiState.Empty
                    } else {
                        RegionUiState.Success(result.data)
                    }
                }
                is Result.Error -> {
                    _uiState.value = RegionUiState.Error(
                        result.message ?: "Error al cargar recetas"
                    )
                }
                is Result.Loading -> Unit
            }
        }
    }
}