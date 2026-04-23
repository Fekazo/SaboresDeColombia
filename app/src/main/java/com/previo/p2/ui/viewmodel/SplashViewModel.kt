package com.previo.p2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.previo.p2.domain.repository.MealRepository
import com.previo.p2.ui.state.SplashUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val mealRepository: MealRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    private val _loadingMessage = MutableStateFlow("Iniciando...")
    val loadingMessage: StateFlow<String> = _loadingMessage.asStateFlow()

    init {
        preloadData()
    }

    private fun preloadData() {
        viewModelScope.launch {
            try {
                _loadingMessage.value = "Cargando platillos populares..."
                val popular = async { mealRepository.getPopularMeals() }
                popular.await()

                _loadingMessage.value = "Cargando todos los platillos..."
                val allMeals = async { mealRepository.preloadAllMeals() }
                allMeals.await()

                _loadingMessage.value = "¡Todo listo!"
                _uiState.value = SplashUiState.Ready("Datos listos")
            } catch (e: Exception) {
                _uiState.value = SplashUiState.Error(e.message ?: "Error al cargar datos")
            }
        }
    }
}