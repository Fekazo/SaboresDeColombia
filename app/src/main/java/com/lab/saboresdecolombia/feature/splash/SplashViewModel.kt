package com.lab.saboresdecolombia.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab.saboresdecolombia.core.domain.usecase.InitializeAppUseCase
import com.lab.saboresdecolombia.core.domain.usecase.ProgressData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SplashUiState(
    val progress: Float = 0f,
    val message: String = "Iniciando...",
    val isDone: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val initializeAppUseCase: InitializeAppUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        initialize()
    }

    private fun initialize() {
        viewModelScope.launch {
            initializeAppUseCase { progressData ->
                _uiState.update {
                    it.copy(progress = progressData.progress, message = progressData.message)
                }
            }.onSuccess {
                _uiState.update { it.copy(isDone = true, progress = 1f) }
            }.onFailure { error ->
                _uiState.update { it.copy(error = error.message) }
            }
        }
    }
}
