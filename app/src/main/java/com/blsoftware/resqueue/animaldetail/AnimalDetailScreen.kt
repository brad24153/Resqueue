package com.blsoftware.resqueue.animaldetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage

@Composable
fun AnimalDetailScreen(
    viewModel: AnimalDetailViewModel,
    modifier: Modifier = Modifier
) {
    val animal by viewModel.animal.collectAsStateWithLifecycle()

    animal?.let { animalData ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            AsyncImage(
                model = animalData.coverPhotoUrl,
                contentDescription = "Photo of ${animalData.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = animalData.name,
                style = MaterialTheme.typography.headlineLarge
            )

            Text(
                text = animalData.breed,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Status: ${animalData.status}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}