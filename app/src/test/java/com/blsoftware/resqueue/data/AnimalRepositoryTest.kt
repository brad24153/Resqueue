package com.blsoftware.resqueue.data

import com.blsoftware.resqueue.data.local.AnimalDao
import com.blsoftware.resqueue.data.local.AnimalEntity
import com.blsoftware.resqueue.data.remote.ShelterluvAnimalDto
import com.blsoftware.resqueue.data.remote.ShelterluvApi
import com.blsoftware.resqueue.data.remote.ShelterluvResponseDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class AnimalRepositoryTest {

    private lateinit var api: ShelterluvApi
    private lateinit var dao: AnimalDao
    private lateinit var repository: AnimalRepository

    @BeforeEach
    fun setup() {
        api = mockk()
        dao = mockk()

    }

    @AfterEach
    fun teardown() {
        unmockkAll()
    }

    @Test
    @DisplayName("animals flow observes all animals from DAO")
    fun animalsFlowObservesAllAnimalsFromDao() = runTest {
        // Given
        val fakeEntities = listOf(
            createTestAnimalEntity(id = "1", name = "Rover"),
            createTestAnimalEntity(id = "2", name = "Whiskers")
        )
        every { dao.getAllAnimals() } returns flowOf(fakeEntities)

        // When
        repository = AnimalRepository(api, dao)
        val result = repository.animals.first()

        // Then
        assertEquals(fakeEntities, result)
        coVerify(exactly = 1) { dao.getAllAnimals() }
    }

    @Test
    @DisplayName("refreshAnimals fetches DTOs from API, transforms to entities, and inserts to DAO")
    fun refreshAnimalsFetchesTransformsAndInserts() = runTest {
        // Given
        val fakeDto1 = mockk<ShelterluvAnimalDto>()
        val fakeDto2 = mockk<ShelterluvAnimalDto>()
        val fakeEntity1 = createTestAnimalEntity(id = "101", name = "Shadow")
        val fakeEntity2 = createTestAnimalEntity(id = "102", name = "Bella")

        // Mock extension function `toEntity()` on ShelterluvAnimalDto
        mockkStatic("com.blsoftware.resqueue.data.AnimalMapperKt") // Adjust filename if mapper file is named differently
        every { fakeDto1.toEntity() } returns fakeEntity1
        every { fakeDto2.toEntity() } returns fakeEntity2

        val fakeResponse = ShelterluvResponseDto(animals = listOf(fakeDto1, fakeDto2))
        coEvery { api.getAnimals() } returns fakeResponse
        coEvery { dao.insertAnimals(any()) } returns Unit
        every { dao.getAllAnimals() } returns flowOf(emptyList())

        // When
        repository = AnimalRepository(api, dao)
        val result = repository.refreshAnimals()

        // Then
        assertEquals(Result.success(Unit), result)
        coVerify(exactly = 1) { api.getAnimals() }
        coVerify(exactly = 1) { dao.insertAnimals(listOf(fakeEntity1, fakeEntity2)) }
    }

    @Test
    @DisplayName("getAnimalById observes filtered animal from DAO")
    fun getAnimalByIdObservesFilteredAnimalFromDao() = runTest {
        // Given
        val animalId = "42"
        val expectedEntity = createTestAnimalEntity(id = animalId, name = "Max")
        every { dao.getAnimalById(animalId) } returns flowOf(expectedEntity)
        every { dao.getAllAnimals() } returns flowOf(emptyList())

        // When
        repository = AnimalRepository(api, dao)
        val result = repository.getAnimalById(animalId).first()

        // Then
        assertEquals(expectedEntity, result)
        coVerify(exactly = 1) { dao.getAnimalById(animalId) }
    }

    @Test
    @DisplayName("getAnimalById returns null flow when animal is not found")
    fun getAnimalByIdReturnsNullWhenNotFound() = runTest {
        // Given
        val missingId = "999"
        every { dao.getAnimalById(missingId) } returns flowOf(null)
        every { dao.getAllAnimals() } returns flowOf(emptyList())

        // When
        repository = AnimalRepository(api, dao)
        val result = repository.getAnimalById(missingId).first()

        // Then
        assertNull(result)
        coVerify(exactly = 1) { dao.getAnimalById(missingId) }
    }

    @Test
    @DisplayName("refreshAnimals returns Result.failure with NetworkError when API throws IOException")
    fun refreshAnimalsReturnsNetworkErrorOnIOException() = runTest {
        // Given
        val ioException = java.io.IOException("No Internet Connection")
        coEvery { api.getAnimals() } throws ioException
        every { dao.getAllAnimals() } returns flowOf(emptyList())

        // When
        repository = AnimalRepository(api, dao)
        val result = repository.refreshAnimals()

        // Then
        org.junit.jupiter.api.Assertions.assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        org.junit.jupiter.api.Assertions.assertTrue(exception is DataException.NetworkError)
        assertEquals(ioException, exception?.cause)
        coVerify(exactly = 0) { dao.insertAnimals(any()) }
    }

    @Test
    @DisplayName("refreshAnimals returns Result.failure with UnknownError when API throws generic Exception")
    fun refreshAnimalsReturnsUnknownErrorOnGenericException() = runTest {
        // Given
        val genericException = RuntimeException("500 Internal Server Error")
        coEvery { api.getAnimals() } throws genericException
        every { dao.getAllAnimals() } returns flowOf(emptyList())

        // When
        repository = AnimalRepository(api, dao)
        val result = repository.refreshAnimals()

        // Then
        org.junit.jupiter.api.Assertions.assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        org.junit.jupiter.api.Assertions.assertTrue(exception is DataException.UnknownError)
        assertEquals(genericException, exception?.cause)
        coVerify(exactly = 0) { dao.insertAnimals(any()) }
    }

    @Test
    fun testAgeCategoryEnum() {

    }

}

fun createTestAnimalEntity(
    id: String = "1",
    name: String = "Test Animal",
    breed: String = "Test Breed",
    gender: String = "Test Male",
    altered: AlteredStatus = AlteredStatus.UNKNOWN,
    age: Int = 44,
    ageCategory: AgeCategory = AgeCategory.ADULT,
    coverPhotoUrl: String? = "https://test.test/test.jpg",
    status: String = "Test Status",
    currentLocationOrFoster: String = "Test Location",
    daysInRescue: Int = 10
) = AnimalEntity(
    id = id,
    name = name,
    breed = breed,
    gender = gender,
    altered = altered,
    age = age,
    ageCategory = ageCategory,
    coverPhotoUrl = coverPhotoUrl,
    status = status,
    currentLocationOrFoster = currentLocationOrFoster,
    daysInRescue = daysInRescue
)