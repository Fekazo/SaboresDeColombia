package com.previo.p2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.previo.p2.domain.repository.MealRepository
import com.previo.p2.domain.util.Result
import com.previo.p2.ui.state.PopularUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(
    private val mealRepository: MealRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PopularUiState>(PopularUiState.Loading)
    val uiState: StateFlow<PopularUiState> = _uiState.asStateFlow()

    init {
        loadPopularMeals()
    }

    fun loadPopularMeals() {
        viewModelScope.launch {
            _uiState.value = PopularUiState.Loading
            repeat(3) { attempt ->
                val result = mealRepository.getPopularMeals()
                when (result) {
                    is Result.Success -> {
                        _uiState.value = PopularUiState.Success(result.data)
                        return@launch
                    }
                    is Result.Error -> {
                        if (attempt == 2) {
                            _uiState.value = PopularUiState.Error(
                                result.message ?: "Error al cargar recetas"
                            )
                        }
                    }
                    is Result.Loading -> Unit
                }
            }
        }
    }
}