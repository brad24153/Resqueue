package com.blsoftware.resqueue.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

interface ShelterluvApi {

    @GET("animals")
    suspend fun getAnimals(
        @Query("status_type") statusType: String = "publishable"
    ): ShelterluvResponseDto
}

@Serializable
data class ShelterluvResponseDto(
    @SerialName("animals") val animals: List<ShelterluvAnimalDto>
)