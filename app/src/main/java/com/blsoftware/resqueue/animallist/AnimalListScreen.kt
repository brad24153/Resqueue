package com.blsoftware.resqueue.animallist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.blsoftware.resqueue.R
import com.blsoftware.resqueue.data.AgeCategory
import com.blsoftware.resqueue.data.AlteredStatus

// Stateful screen
@Composable
fun AnimalListScreen(
    viewModel: AnimalListViewModel,
    onAnimalClick: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filterOptions by viewModel.filterSortOptions.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    AnimalListContent(
        uiState = uiState,
        filterOptions = filterOptions,
        isRefreshing = isRefreshing,
        onAnimalClick = onAnimalClick,
        onBackClick = onBackClick,
        onRetryClick = { viewModel.refresh(isPullToRefresh = false) },
        onRefresh = { viewModel.refresh(isPullToRefresh = true) },
        onSearchQueryChange = viewModel::onSearchQueryChanged,
        onAlteredStatusSelect = viewModel::onAlteredStatusSelected,
        onGenderSelect = viewModel::onGenderSelected,
        onAgeCategorySelect = viewModel::onAgeCategorySelected,
        onSortPropertySelect = viewModel::onSortPropertySelected,
        onToggleSortDirection = viewModel::onToggleSortDirection,
        onClearFilters = viewModel::clearFilters,
        modifier = modifier
    )
}

// Stateless content for UI testing with dummy data
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalListContent(
    uiState: AnimalUiState,
    filterOptions: AnimalFilterOptions,
    isRefreshing: Boolean,
    onAnimalClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit,
    onRefresh: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onAlteredStatusSelect: (AlteredStatus) -> Unit,
    onGenderSelect: (String) -> Unit,
    onAgeCategorySelect: (AgeCategory) -> Unit,
    onSortPropertySelect: (AnimalSortProperty) -> Unit,
    onToggleSortDirection: () -> Unit,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showSortMenu by remember { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val activeFilterCount = filterOptions.gender.size +
            filterOptions.ageCategory.size +
            filterOptions.alteredStatus.size


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    DockedSearchBar(
                        inputField = {
                            SearchBarDefaults.InputField(
                                query = filterOptions.searchQuery,
                                onQueryChange = onSearchQueryChange,
                                onSearch = { },
                                expanded = false,
                                onExpandedChange = { },
                                placeholder = {
                                    Text(
                                        text = stringResource(R.string.animal_list_search_placeholder),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                trailingIcon = {
                                    if (filterOptions.searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { onSearchQueryChange("") }) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = stringResource(R.string.clear_search),
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            )
                        },
                        expanded = false,
                        onExpandedChange = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp)
                    ) {}
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.nav_back)
                        )
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Sort,
                                contentDescription = stringResource(R.string.animal_list_sort_by)
                            )
                        }

                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            // --- Sort Property Section ---
                            Text(
                                text = stringResource(R.string.animal_list_sort_by),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )

                            AnimalSortProperty.entries.forEach { property ->
                                val isSelected = filterOptions.sortProperty == property
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            when (property) {
                                                AnimalSortProperty.NAME -> stringResource(R.string.animal_list_sort_by_name)
                                                AnimalSortProperty.DAYS_IN_RESCUE -> stringResource(
                                                    R.string.animal_list_sort_by_days_in_rescue
                                                )

                                                AnimalSortProperty.AGE -> stringResource(R.string.animal_list_sort_by_age)
                                            }
                                        )
                                    },
                                    trailingIcon = {
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = stringResource(R.string.animal_list_selected),
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    },
                                    onClick = {
                                        onSortPropertySelect(property)
                                        showSortMenu = false
                                    }
                                )
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                            // --- Sort Direction Section ---
                            Text(
                                text = stringResource(R.string.animal_list_sort_order),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )

                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.animal_list_sort_direction_ascending)) },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.ArrowUpward,
                                        contentDescription = null
                                    )
                                },
                                trailingIcon = {
                                    if (filterOptions.sortDirection == SortDirection.ASCENDING) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = stringResource(R.string.animal_list_selected),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                },
                                onClick = {
                                    if (filterOptions.sortDirection != SortDirection.ASCENDING) {
                                        onToggleSortDirection()
                                    }
                                    showSortMenu = false
                                }
                            )

                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.animal_list_sort_direction_descending)) },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.ArrowDownward,
                                        contentDescription = null
                                    )
                                },
                                trailingIcon = {
                                    if (filterOptions.sortDirection == SortDirection.DESCENDING) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = stringResource(R.string.animal_list_selected),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                },
                                onClick = {
                                    if (filterOptions.sortDirection != SortDirection.DESCENDING) {
                                        onToggleSortDirection()
                                    }
                                    showSortMenu = false
                                }
                            )
                        }
                    }
                    BadgedBox(
                        badge = {
                            if (activeFilterCount > 0) {
                                Badge { Text(activeFilterCount.toString()) }
                            }
                        }
                    ) {
                        IconButton(onClick = { showFilterSheet = true }) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = stringResource(R.string.animal_list_filter_options)
                            )
                        }
                    }
                }

            )


        },
        modifier = modifier
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = uiState) {
                is AnimalUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is AnimalUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onRetryClick) {
                            Text(stringResource(R.string.animal_list_retry))
                        }
                    }
                }

                is AnimalUiState.Success -> {
                    val listState = rememberLazyListState()
                    LaunchedEffect(filterOptions) {
                        if (listState.firstVisibleItemIndex > 0) {
                            listState.animateScrollToItem(0)
                        }
                    }

                    PullToRefreshBox(
                        isRefreshing = isRefreshing,
                        onRefresh = onRefresh,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = pluralStringResource(
                                    R.plurals.animal_count_found,
                                    state.animals.size,
                                    state.animals.size
                                ),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                            )

                            if (state.animals.isEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val isFiltering = filterOptions.searchQuery.isNotBlank() ||
                                            filterOptions.alteredStatus.isNotEmpty() ||
                                            filterOptions.gender.isNotEmpty() ||
                                            filterOptions.ageCategory.isNotEmpty()


                                    Text(
                                        text = if (isFiltering) {
                                            stringResource(R.string.animal_list_no_animals_match_filters)
                                        } else {
                                            stringResource(R.string.animal_list_no_animals_available)
                                        },
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            } else {
                                LazyColumn(
                                    state = listState,
                                    contentPadding = PaddingValues(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(
                                        items = state.animals,
                                        key = { animal -> animal.id }
                                    ) { animal ->
                                        AnimalListItem(
                                            animal = animal,
                                            onAnimalClick = onAnimalClick
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            sheetState = sheetState
        ) {
            FilterBottomSheetContent(
                options = filterOptions,
                onAlteredStatusSelect = onAlteredStatusSelect,
                onGenderSelect = onGenderSelect,
                onAgeCategorySelect = onAgeCategorySelect,
                onClearFilters = onClearFilters,
                onApplyFilters = { showFilterSheet = false }
            )
        }

    }

}


@Composable
private fun FilterBottomSheetContent(
    options: AnimalFilterOptions,
    onAlteredStatusSelect: (AlteredStatus) -> Unit,
    onGenderSelect: (String) -> Unit,
    onAgeCategorySelect: (AgeCategory) -> Unit,
    onClearFilters: () -> Unit,
    onApplyFilters: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = stringResource(R.string.animal_list_filter_animals),
            style = MaterialTheme.typography.titleMedium
        )

        // --- Gender Section ---
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(R.string.animal_list_filter_gender),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    "Male" to stringResource(R.string.animal_list_filter_gender_male),
                    "Female" to stringResource(R.string.animal_list_filter_gender_female)
                ).forEach { (genderKey, labelRes) ->
                    val isSelected = options.gender.any { it.equals(genderKey, ignoreCase = true) }
                    FilterChip(
                        selected = isSelected,
                        onClick = { onGenderSelect(genderKey) },
                        label = { Text(labelRes) }
                    )
                }
            }
        }

        // --- Age Category Section ---
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(R.string.animal_list_filter_age_group),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AgeCategory.entries.filter {it != AgeCategory.UNKNOWN}.forEach { category ->
                    val isSelected = options.ageCategory.contains(category)
                    FilterChip(
                        selected = isSelected,
                        onClick = { onAgeCategorySelect(category) },
                        label = { Text(category.label) }
                    )
                }
            }
        }

        // --- Altered Status Section ---
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(R.string.animal_list_filter_altered_status),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AlteredStatus.entries.forEach { status ->
                    val isSelected = options.alteredStatus.contains(status)
                    FilterChip(
                        selected = isSelected,
                        onClick = { onAlteredStatusSelect(status) },
                        label = {
                            Text(
                                stringResource(status.labelResId),
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    )
                }

            }
        }


        HorizontalDivider()

        // --- Bottom Action Row ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val hasActiveFilters = options.gender.isNotEmpty() ||
                    options.ageCategory.isNotEmpty() ||
                    options.alteredStatus.isNotEmpty()

            TextButton(
                onClick = onClearFilters,
                enabled = hasActiveFilters
            ) {
                Text(stringResource(R.string.animal_list_filter_reset))
            }

            Button(
                onClick = onApplyFilters
            ) {
                Text(stringResource(R.string.animal_list_filter_apply))
            }
        }
    }
}

