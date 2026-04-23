package com.previo.p2.ui.state

sealed class SplashUiState {
    data object Loading : SplashUiState()
    data class Ready(val message: String) : SplashUiState()
    data class Error(val message: String) : SplashUiState()
}