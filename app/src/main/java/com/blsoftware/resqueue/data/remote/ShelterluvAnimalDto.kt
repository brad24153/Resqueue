package com.blsoftware.resqueue.data.remote

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/* COVERAGE-START */
/**
 * AUTO-GENERATED COVERAGE METRICS (2026-10-11)
 * - Line Coverage:   25 / 27 (92%)
 * - Branch Coverage: 0 / 0 (100%)
 */
/* COVERAGE-END */

@Serializable
data class ShelterluvAnimalDto(
    @SerialName("ID") val id: String? = null,
    @SerialName("Name") val name: String? = null,
    @SerialName("Breed") val breed: String? = null,
    @SerialName("Age") val age: Int? = null,
    @SerialName("Sex") val gender: String? = null,
    @SerialName("Altered") val altered: String? = null,
    @SerialName("CoverPhoto") val coverPhotoUrl: String? = null,
    @SerialName("Status") val status: String? = null,
    @SerialName("CurrentLocation") val currentLocation: LocationDto?,
    @SerialName("AssociatedPerson") val associatedPerson: AssociatedPersonDto?,
    @SerialName("LastIntakeUnixTime") val lastIntakeUnixTime: Long? = null

)


@Serializable
data class ShelterluvResponse(
    @SerializedName("animals") val animals: List<ShelterluvAnimalDto> = emptyList()
)

@Serializable
data class AssociatedPersonDto(
    @SerialName("FirstName") val firstName: String? = null,
    @SerialName("LastName") val lastName: String? = null
)

@Serializable
data class LocationDto(
    @SerialName("Tier1") val tier1: String? = null
)


