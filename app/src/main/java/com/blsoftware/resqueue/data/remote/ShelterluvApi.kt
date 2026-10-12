package com.blsoftware.resqueue.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface ShelterluvApi {

    @GET("animals")
    suspend fun getAnimals(
        @Query("status_type") statusType: String = "publishable"
    ): ShelterluvResponseDto
}

data class ShelterluvResponseDto(
    @SerializedName("animals") val animals: List<ShelterluvAnimalDto>
)