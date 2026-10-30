package com.blsoftware.resqueue.animallist

import com.blsoftware.resqueue.data.AgeCategory
import com.blsoftware.resqueue.data.AlteredStatus
import com.blsoftware.resqueue.data.local.AnimalEntity

enum class AnimalSortProperty {
    NAME,
    AGE,
    DAYS_IN_RESCUE
}

enum class SortDirection {
    ASCENDING,
    DESCENDING;

    fun toggle(): SortDirection {
        return when (this) {
            ASCENDING -> DESCENDING
            DESCENDING -> ASCENDING
        }
    }
}

data class AnimalFilterOptions(
    val searchQuery: String = "",
    val alteredStatus: Set<AlteredStatus> = emptySet(),
    val gender: Set<String> = emptySet(),
    val ageCategory: Set<AgeCategory> = emptySet(),
    val sortProperty: AnimalSortProperty = AnimalSortProperty.DAYS_IN_RESCUE,
    val sortDirection: SortDirection = SortDirection.ASCENDING
)


sealed interface AnimalUiState {
    data object Loading : AnimalUiState
    data class Success(val animals: List<AnimalEntity>, val filterSortOptions: AnimalFilterOptions) : AnimalUiState
    data class Error(val message: String) : AnimalUiState
}