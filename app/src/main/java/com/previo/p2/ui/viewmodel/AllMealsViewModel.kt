package com.previo.p2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.previo.p2.domain.repository.MealRepository
import com.previo.p2.domain.util.Result
import com.previo.p2.ui.components.SortOrder
import com.previo.p2.ui.state.AllMealsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllMealsViewModel @Inject constructor(
    private val mealRepository: MealRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AllMealsUiState>(AllMealsUiState.Loading)
    val uiState: StateFlow<AllMealsUiState> = _uiState.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.DEFAULT)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    init {
        loadAllMeals()
    }

    fun loadAllMeals() {
        viewModelScope.launch {
            _uiState.value = AllMealsUiState.Loading
            when (val result = mealRepository.getAllColombianMeals()) {
                is Result.Success -> {
                    _uiState.value = if (result.data.isEmpty()) {
                        AllMealsUiState.Empty
                    } else {
                        AllMealsUiState.Success(result.data)
                    }
                }
                is Result.Error -> {
                    _uiState.value = AllMealsUiState.Error(
                        result.message ?: "Error al cargar platillos"
                    )
                }
                is Result.Loading -> Unit
            }
        }
    }

    fun setSortOrder(order: SortOrder) {
        _sortOrder.value = order
    }
}