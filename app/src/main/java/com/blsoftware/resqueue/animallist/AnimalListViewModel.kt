package com.blsoftware.resqueue.animallist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.blsoftware.resqueue.data.DataException
import com.blsoftware.resqueue.data.AnimalRepository
import javax.inject.Inject

@HiltViewModel
class AnimalListViewModel @Inject constructor(
    private val repository: AnimalRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    private val _errorMessage = MutableStateFlow<String?>(null)

    init {
        refresh()
    }

    val uiState: StateFlow<AnimalUiState> = combine(
        repository.animals,
        _isLoading,
        _errorMessage
    ) { animals, isLoading, error ->
        when {
            // Show error screen if fetch failed and local DB is empty
            error != null && animals.isEmpty() -> AnimalUiState.Error(error)
            // Show spinner while active initial fetch is in progress and local DB is empty
            isLoading && animals.isEmpty() -> AnimalUiState.Loading
            // Render content (or empty state message) once data arrives or fetch finishes
            else -> AnimalUiState.Success(animals)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AnimalUiState.Loading
    )

    fun refresh() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            repository.refreshAnimals()
                .onSuccess {
                    // Animals refreshed successfully
                }
                .onFailure { error ->
                    _errorMessage.value = when (error) {
                        is DataException.NetworkError -> "No internet connection."
                        else -> "Failed to fetch animals."
                    }
                }

            _isLoading.value = false
        }
    }
}