package com.previo.p2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.previo.p2.domain.repository.MealRepository
import com.previo.p2.domain.util.Result
import com.previo.p2.ui.components.SortOrder
import com.previo.p2.ui.state.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val mealRepository: MealRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.DEFAULT)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
        searchJob?.cancel()
        if (newQuery.isBlank()) {
            _uiState.value = SearchUiState.Idle
            return
        }
        searchJob = viewModelScope.launch {
            delay(400)
            search(newQuery)
        }
    }

    private suspend fun search(query: String) {
        _uiState.value = SearchUiState.Loading
        when (val result = mealRepository.searchMeals(query)) {
            is Result.Success -> {
                _uiState.value = if (result.data.isEmpty()) {
                    SearchUiState.Empty(query)
                } else {
                    SearchUiState.Success(result.data)
                }
            }
            is Result.Error -> {
                _uiState.value = SearchUiState.Error(result.message ?: "Error en la búsqueda")
            }
            is Result.Loading -> Unit
        }
    }

    fun setSortOrder(order: SortOrder) {
        _sortOrder.value = order
    }

    fun clearQuery() {
        _query.value = ""
        _uiState.value = SearchUiState.Idle
        _sortOrder.value = SortOrder.DEFAULT
    }
}