package com.blsoftware.resqueue.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.blsoftware.resqueue.data.AgeCategory
import com.blsoftware.resqueue.data.AlteredStatus


@Entity(tableName = "animals")
data class AnimalEntity(
    @PrimaryKey val id: String,
    val name: String,
    val breed: String,
    val gender: String,
    val altered: AlteredStatus,
    val age: Int,
    val ageCategory: AgeCategory,
    val coverPhotoUrl: String?,
    val status: String,
    val currentLocationOrFoster: String,
    val daysInRescue: Int
)

