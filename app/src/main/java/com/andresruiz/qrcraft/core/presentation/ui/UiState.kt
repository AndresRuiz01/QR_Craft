package com.andresruiz.qrcraft.core.presentation.ui

sealed interface UiState<out T> {
    data class Success<T>(val state: T) : UiState<T>
    object Error : UiState<Nothing>
    object Loading : UiState<Nothing>
}