package com.blsoftware.resqueue.animallist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.blsoftware.resqueue.R


//Stateful screen
@Composable
fun AnimalListScreen(
    viewModel: AnimalListViewModel,
    onAnimalClick: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AnimalListContent(
        uiState = uiState,
        onAnimalClick = onAnimalClick,
        onBackClick = onBackClick,
        onRetryClick = { viewModel.refresh() },
        modifier = modifier
    )

}


//Stateless content for UI testing with dummy data
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalListContent(
    uiState: AnimalUiState,
    onAnimalClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.in_custody_animal_list)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate Back"
                        )
                    }
                }
            )
        }
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
                            text = "Failed to load animals",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onRetryClick) {
                            Text("Retry")
                        }
                    }
                }
                is AnimalUiState.Success -> {
                    if (state.animals.isEmpty()) {
                        Text(
                            text = "No animals available right now.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = state.animals,
                                key = { animal -> animal.id } // Stable key for optimal recomposition
                            ) { animal ->
                                AnimalListItem(
                                    animal = animal,
                                    onAnimalClick = onAnimalClick)
                            }
                        }
                    }
                }
            }
        }
    }
}