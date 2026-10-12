package com.blsoftware.resqueue.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimalDao {
    @Query("SELECT * FROM animals")
    fun getAllAnimals(): Flow<List<AnimalEntity>>

    @Query("SELECT * FROM animals WHERE id = :animalId")
    fun getAnimalById(animalId: String): Flow<AnimalEntity?>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimals(animals: List<AnimalEntity>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAnimal(animal: AnimalEntity)

    @Query("DELETE FROM animals")
    suspend fun clearAnimals()
}