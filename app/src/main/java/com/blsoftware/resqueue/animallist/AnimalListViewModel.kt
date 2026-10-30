package com.blsoftware.resqueue.animallist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blsoftware.resqueue.data.AgeCategory
import com.blsoftware.resqueue.data.AlteredStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.blsoftware.resqueue.data.DataException
import com.blsoftware.resqueue.data.AnimalRepository
import com.blsoftware.resqueue.data.local.AnimalEntity
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AnimalListViewModel @Inject constructor(
    private val repository: AnimalRepository
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    private val _isLoading = MutableStateFlow(true)
    private val _errorMessage = MutableStateFlow<String?>(null)

    private val _filterSortOptions = MutableStateFlow(AnimalFilterOptions())
    val filterSortOptions: StateFlow<AnimalFilterOptions> = _filterSortOptions


    init {
        refresh()
    }

    val uiState: StateFlow<AnimalUiState> = combine(
        repository.animals,
        _filterSortOptions,
        _isLoading,
        _errorMessage
    ) { animals, options, isLoading, error ->
        when {
            // Show error screen if fetch failed and local DB is empty
            error != null && animals.isEmpty() -> AnimalUiState.Error(error)
            // Show spinner while active initial fetch is in progress and local DB is empty
            isLoading && animals.isEmpty() -> AnimalUiState.Loading
            // Render content (or empty state message) once data arrives or fetch finishes
            else -> {
                val processedAnimalList = filterAndSortAnimals(animals, options)
                AnimalUiState.Success(
                    animals = processedAnimalList,
                    filterSortOptions = options
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AnimalUiState.Loading
    )

    fun refresh(isPullToRefresh: Boolean = false) {
        viewModelScope.launch {
            if (isPullToRefresh) {
                _isRefreshing.value = true
            } else {
                _isLoading.value = true
            }

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
            _isRefreshing.value = false
        }
    }

    fun onSearchQueryChanged(query: String) {
        _filterSortOptions.update { it.copy(searchQuery = query) }
    }

    fun onAlteredStatusSelected(alteredStatus: AlteredStatus) {
        _filterSortOptions.update { options ->
            val current = options.alteredStatus
            val updated = if (current.contains(alteredStatus)) current - alteredStatus else current + alteredStatus
            options.copy(alteredStatus = updated)
        }
    }

    fun onGenderSelected(gender: String) {
        _filterSortOptions.update { options ->
            val current = options.gender
            val updated = if (current.contains(gender)) current - gender else current + gender
            options.copy(gender = updated)
        }
    }

    fun onAgeCategorySelected(ageCategory: AgeCategory) {
        _filterSortOptions.update { options ->
            val current = options.ageCategory
            val updated = if (current.contains(ageCategory)) current - ageCategory else current + ageCategory
            options.copy(ageCategory = updated)
        }
    }

    fun onSortPropertySelected(property: AnimalSortProperty) {
        _filterSortOptions.update { it.copy(sortProperty = property) }
    }

    fun onToggleSortDirection() {
        _filterSortOptions.update { it.copy(sortDirection = it.sortDirection.toggle()) }
    }

    fun clearFilters() {
        _filterSortOptions.value = AnimalFilterOptions()
    }
}

private fun filterAndSortAnimals(
    animals: List<AnimalEntity>,
    options: AnimalFilterOptions
): List<AnimalEntity> {
    val comparator: Comparator<AnimalEntity> = when (options.sortProperty) {
        AnimalSortProperty.NAME -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
        AnimalSortProperty.DAYS_IN_RESCUE -> compareBy { it.daysInRescue }
        AnimalSortProperty.AGE -> compareBy { it.age }
    }

    val finalComparator = if (options.sortDirection == SortDirection.DESCENDING) {
        comparator.reversed()
    } else {
        comparator
    }

    return animals
        .filter { animal ->
            // 1. Free-text search (checks name, breed, location, or status)
            val matchesSearch = options.searchQuery.isBlank() ||
                    animal.name.contains(options.searchQuery, ignoreCase = true) ||
                    animal.breed.contains(options.searchQuery, ignoreCase = true) ||
                    animal.currentLocationOrFoster.contains(
                        options.searchQuery,
                        ignoreCase = true
                    ) ||
                    animal.status.contains(options.searchQuery, ignoreCase = true)

            // 2. Enum status check (Spayed/Neutered/Unknown)
            val matchesAltered = options.alteredStatus.isEmpty() || animal.altered in options.alteredStatus

            // 6. Gender check
            val matchesGender = options.gender.isEmpty() ||
                    options.gender.any { it.equals(animal.gender, ignoreCase = true) }

            // 7. Age Range check (e.g., 0..12 for puppies/kittens under 1 year)
            val matchesAgeCategory = options.ageCategory.isEmpty() || animal.ageCategory in options.ageCategory

            // All criteria must evaluate to true
            matchesSearch &&
                    matchesAltered &&
                    matchesGender &&
                    matchesAgeCategory
        }
        .sortedWith(finalComparator)
}
