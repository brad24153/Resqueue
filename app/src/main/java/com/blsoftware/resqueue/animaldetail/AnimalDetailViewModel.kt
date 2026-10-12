package com.blsoftware.resqueue.animaldetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import com.blsoftware.resqueue.data.AnimalRepository
import com.blsoftware.resqueue.data.local.AnimalEntity
import javax.inject.Inject

@HiltViewModel
class AnimalDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: AnimalRepository
) : ViewModel() {

    // Retrieve animalId directly from SavedStateHandle matching the route key
    val animalId: String = checkNotNull(savedStateHandle["animalId"])

    // Fetch the specific animal from Room/Repository using animalId
    val animal: StateFlow<AnimalEntity?> = repository.getAnimalById(animalId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )
}