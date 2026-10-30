package com.blsoftware.resqueue.data

import androidx.annotation.StringRes
import com.blsoftware.resqueue.R
import com.blsoftware.resqueue.data.local.AnimalEntity
import com.blsoftware.resqueue.data.remote.ShelterluvAnimalDto
import java.util.concurrent.TimeUnit


fun ShelterluvAnimalDto.toEntity(): AnimalEntity {
    return AnimalEntity(
        id = this.id ?: "Unknown",
        name = this.name ?: "Unknown",
        breed = this.breed ?: "Unknown",
        age = this.age ?: 1200,
        ageCategory = AgeCategory.fromMonths(this.age),
        gender = this.gender ?: "Unknown",
        altered = AlteredStatus.fromString(altered),
        coverPhotoUrl = this.coverPhotoUrl,
        status = this.status ?: "Unknown",
        currentLocationOrFoster = parseLocationOrFoster(
            locationTier1 = currentLocation?.tier1,
            fosterFirstName = associatedPerson?.firstName,
            fosterLastName = associatedPerson?.lastName
        ),
        daysInRescue = parseDaysInRescue(lastIntakeUnixTime)
    )
}

fun parseLocationOrFoster(
    locationTier1: String?,
    fosterFirstName: String?,
    fosterLastName: String?
): String {

    locationTier1?.takeUnless { it.isBlank() }?.let { location ->
        return "Location: $location"
    }

    val fosterName = listOfNotNull(fosterFirstName, fosterLastName)
        .filter { it.isNotBlank() }
        .joinToString(" ")

    return if (fosterName.isNotBlank()) {
        "Foster: $fosterName"
    } else {
        "Location: Unknown"
    }
}

fun parseDaysInRescue(lastIntakeUnixSeconds: Long?): Int {
    if (lastIntakeUnixSeconds == null || lastIntakeUnixSeconds < 0) return 9999

    val currentUnixSeconds = System.currentTimeMillis() / 1000L
    val diffSeconds = currentUnixSeconds - lastIntakeUnixSeconds

    if (diffSeconds < 0) return 9999

    val fullDays = TimeUnit.SECONDS.toDays(diffSeconds).toInt()
    return fullDays + 1 // +1 so the day of intake is counted as Day 1
}