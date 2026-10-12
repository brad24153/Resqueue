package com.blsoftware.resqueue.animallist

import com.blsoftware.resqueue.data.local.AnimalEntity

sealed interface AnimalUiState {
    data object Loading : AnimalUiState
    data class Success(val animals: List<AnimalEntity>) : AnimalUiState
    data class Error(val message: String) : AnimalUiState
}