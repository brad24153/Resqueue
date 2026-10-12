package com.blsoftware.resqueue.data

import kotlinx.coroutines.flow.Flow
import com.blsoftware.resqueue.data.local.AnimalDao
import com.blsoftware.resqueue.data.local.AnimalEntity
import com.blsoftware.resqueue.data.remote.ShelterluvApi
import java.io.IOException
import javax.inject.Inject

class AnimalRepository @Inject constructor(
    private val api: ShelterluvApi,
    private val dao: AnimalDao
) {
    val animals: Flow<List<AnimalEntity>> = dao.getAllAnimals()

    suspend fun refreshAnimals(): Result<Unit> {
        return try {
            val response = api.getAnimals()
            val entities = response.animals.map { it.toEntity() }
            dao.insertAnimals(entities)
            Result.success(Unit)
        } catch (e: IOException) {
            Result.failure(DataException.NetworkError(e))
        } catch (e: Exception) {
            Result.failure(DataException.UnknownError(e))
        }

    }



    fun getAnimalById(animalId: String): Flow<AnimalEntity?> {
        return dao.getAnimalById(animalId)
    }
}