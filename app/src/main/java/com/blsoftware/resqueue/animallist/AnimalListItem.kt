package com.blsoftware.resqueue.animallist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.blsoftware.resqueue.R
import com.blsoftware.resqueue.data.local.AnimalEntity
import coil3.compose.AsyncImage
import com.blsoftware.resqueue.data.AlteredStatus
import com.blsoftware.resqueue.ui.components.AnimalAgeText
import com.blsoftware.resqueue.ui.theme.ResqueueTheme

@Composable
fun AnimalListItem(
    animal: AnimalEntity,
    onAnimalClick: (String) -> Unit
) {
    // 1. Determine if card should be tinted based on status
    val isUnalteredOrUnknown = animal.altered == AlteredStatus.NO || animal.altered == AlteredStatus.UNKNOWN

    // 2. Select card colors (Container background + content text default)
    val cardColors = if (isUnalteredOrUnknown) {
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    } else {
        CardDefaults.cardColors() // Defaults to standard surface color
    }



    Card(
        onClick = { onAnimalClick(animal.id) },
        colors = cardColors,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Display photo with Coil image loader
            AsyncImage(
                model = animal.coverPhotoUrl,
                contentDescription = "Photo of ${animal.name}",
                placeholder = painterResource(R.drawable.ic_cover_photo_placeholder),
                fallback = painterResource(R.drawable.ic_cover_photo_placeholder),
                error = painterResource(R.drawable.ic_cover_photo_placeholder),
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                //Name - Gender - Age
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = animal.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Text(text = "•", style = MaterialTheme.typography.titleMedium)
                    Text(text = animal.gender, style = MaterialTheme.typography.titleMedium)
                    Text(text = "•", style = MaterialTheme.typography.titleMedium)
                    AnimalAgeText(
                        ageInMonths = animal.age,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                //Breed - Altered

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = animal.breed,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(text = "•", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = stringResource(animal.altered.labelResId),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(Modifier.height(4.dp))

                //Location or Foster - Time in Rescue
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    Text(
                        text = animal.currentLocationOrFoster,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(text = "•", style = MaterialTheme.typography.bodySmall)
                    Text(
                        text = pluralStringResource(
                            R.plurals.days_in_rescue,
                            animal.daysInRescue,
                            animal.daysInRescue
                        ),
                        style = MaterialTheme.typography.bodySmall
                    )

                }

                Spacer(Modifier.height(4.dp))

                //Status
                Text(
                    text = animal.status,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                //Text(text = "•", style = MaterialTheme.typography.titleMedium)


            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnimalListItemPreview(modifier: Modifier = Modifier) {
    ResqueueTheme {
        AnimalListItem(
            animal = AnimalEntity(
                id = "123",
                name = "Barnabaus Maximus The Valiant",
                breed = "Beagle",
                status = "Available Foster",
                altered = AlteredStatus.UNKNOWN,
                gender = "Male",
                age = 4,
                coverPhotoUrl = "https://example.com/beagle.jpg",
                currentLocationOrFoster = "Location: The Rescue Ranch",
                daysInRescue = 43
            ),
            onAnimalClick = {}
        )
    }
}