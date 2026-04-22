package com.previo.p2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.previo.p2.domain.repository.MealRepository
import com.previo.p2.domain.util.Result
import com.previo.p2.ui.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mealRepository: MealRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadPopularMeals()
    }

    fun loadPopularMeals() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            repeat(3) { attempt ->
                val result = mealRepository.getPopularMeals()
                when (result) {
                    is Result.Success -> {
                        _uiState.value = HomeUiState.Success(result.data)
                        return@launch
                    }
                    is Result.Error -> {
                        if (attempt == 2) {
                            _uiState.value = HomeUiState.Error(
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