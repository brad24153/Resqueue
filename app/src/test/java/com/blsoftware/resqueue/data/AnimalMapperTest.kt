package com.blsoftware.resqueue.data

import com.blsoftware.resqueue.R
import com.blsoftware.resqueue.data.remote.AssociatedPersonDto
import com.blsoftware.resqueue.data.remote.LocationDto
import com.blsoftware.resqueue.data.remote.ShelterluvAnimalDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.provider.ValueSource
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AnimalMapperTest {

    @Test
    fun nullDtoConstructor() {
        val dto = ShelterluvAnimalDto(
            id = null,
            name = null,
            breed = null,
            gender = null,
            age = null,
            coverPhotoUrl = null,
            status = null,
            altered = null,
            currentLocation = null,
            associatedPerson = null,
            lastIntakeUnixTime = null
        )

        val entity = dto.toEntity()

        assertEquals("Unknown", entity.id)
        assertEquals("Unknown", entity.name)
        assertEquals("Unknown", entity.breed)
        assertEquals("Unknown", entity.gender)
        assertEquals(1200, entity.age)
        assertEquals("", entity.coverPhotoUrl)
        assertEquals(AlteredStatus.UNKNOWN, entity.altered)
        assertEquals("Location: Unknown", entity.currentLocationOrFoster)
        assertEquals(9999, entity.daysInRescue)
    }


    @Test
    fun alteredStatusNull(){
        assertEquals(AlteredStatus.UNKNOWN, AlteredStatus.fromString(null))
    }


    @ParameterizedTest
    @ValueSource(strings = ["Yes", "yes", "YES", "yEs", "yeS", "  yEs  "])
    fun alteredStatusYes(input: String){
        assertEquals(AlteredStatus.YES, AlteredStatus.fromString(input))
    }

    @Test
    fun alteredStatusNo(){
        var dto = createBaseDto().copy(altered = "No")
        var entity = dto.toEntity()
        assertEquals(AlteredStatus.NO, entity.altered)

        dto = dto.copy(altered = "no")
        entity = dto.toEntity()
        assertEquals(AlteredStatus.NO, entity.altered)

        dto = dto.copy(altered = "  nO  ")
        entity = dto.toEntity()
        assertEquals(AlteredStatus.NO, entity.altered)
    }

    @Test
    fun alteredStatusUnknown(){
        var dto = createBaseDto().copy(altered = "Y es")
        var entity = dto.toEntity()
        assertEquals(AlteredStatus.UNKNOWN, entity.altered)

        dto = dto.copy(altered = "  n o")
        entity = dto.toEntity()
        assertEquals(AlteredStatus.UNKNOWN, entity.altered)

        dto = dto.copy(altered = "y3s")
        entity = dto.toEntity()
        assertEquals(AlteredStatus.UNKNOWN, entity.altered)

        dto = dto.copy(altered = "32lkjac8s93 sl")
        entity = dto.toEntity()
        assertEquals(AlteredStatus.UNKNOWN, entity.altered)
    }

    @Test
    fun compareLabelResIdToEnum() {
        assertEquals(R.string.altered, AlteredStatus.YES.labelResId)
        assertEquals(R.string.not_altered, AlteredStatus.NO.labelResId)
        assertEquals(R.string.alter_unknown, AlteredStatus.UNKNOWN.labelResId)
    }

    @ParameterizedTest(name = "location=''{0}'', firstName=''{1}'', lastName=''{2}'' -> ''{3}''")
    @CsvSource(
        // Format: location, fosterFirstName, fosterLastName, expectedResult

        // --- Location: null (9 combinations) ---
        " , , , Location: Unknown",
        " , '  ', , Location: Unknown",
        " , , '  ', Location: Unknown",
        " , '  ', '  ', Location: Unknown",
        " , Jane, , Foster: Jane",
        " , Jane, '  ', Foster: Jane",
        " , , Doe, Foster: Doe",
        " , '  ', Doe, Foster: Doe",
        " , Jane, Doe, Foster: Jane Doe",

        // --- Location: "  " (Blank - 9 combinations) ---
        "'  ', , , Location: Unknown",
        "'  ', '  ', , Location: Unknown",
        "'  ', , '  ', Location: Unknown",
        "'  ', '  ', '  ', Location: Unknown",
        "'  ', Jane, , Foster: Jane",
        "'  ', Jane, '  ', Foster: Jane",
        "'  ', , Doe, Foster: Doe",
        "'  ', '  ', Doe, Foster: Doe",
        "'  ', Jane, Doe, Foster: Jane Doe",

        // --- Location: "The Rescue Ranch" (9 combinations) ---
        "'The Rescue Ranch', , , Location: The Rescue Ranch",
        "'The Rescue Ranch', '  ', , Location: The Rescue Ranch",
        "'The Rescue Ranch', , '  ', Location: The Rescue Ranch",
        "'The Rescue Ranch', '  ', '  ', Location: The Rescue Ranch",
        "'The Rescue Ranch', Jane, , Location: The Rescue Ranch",
        "'The Rescue Ranch', Jane, '  ', Location: The Rescue Ranch",
        "'The Rescue Ranch', , Doe, Location: The Rescue Ranch",
        "'The Rescue Ranch', '  ', Doe, Location: The Rescue Ranch",
        "'The Rescue Ranch', Jane, Doe, Location: The Rescue Ranch"
    )
    fun parseLocationOrFoster_AllInputCombinations(
        location: String?,
        fosterFirstName: String?,
        fosterLastName: String?,
        expected: String
    ) {
        val result = parseLocationOrFoster(location, fosterFirstName, fosterLastName)
        assertEquals(expected, result)
    }

    @Test
    fun parseDaysInRescue_Null() {
        assertEquals(9999, parseDaysInRescue(null))
    }

    @Test
    fun parseDaysInRescue_TwoDays() {
        val twoDaysAgoUnixSeconds = (System.currentTimeMillis() / 1000L) - 172800
        assertEquals(3, parseDaysInRescue(twoDaysAgoUnixSeconds))
    }

    @Test
    fun parseDaysInRescue_Negative() {
        assertEquals(9999, parseDaysInRescue(-100))
    }

    @Test
    fun parseDaysInRescue_Future() {
        val futureUnixSeconds = System.currentTimeMillis() / 1000L + 1000000000
        assertEquals(9999, parseDaysInRescue(futureUnixSeconds))
    }

    @Test
    fun parseDaysInRescue_Current() {
        val currentUnixSeconds = System.currentTimeMillis() / 1000L
        assertEquals(1, parseDaysInRescue(currentUnixSeconds))
    }

}



private fun createBaseDto() = ShelterluvAnimalDto(
    id = "123",
    name = "Nyala",
    breed = "Domestic Shorthair",
    gender = "Female",
    age = 12,
    status = "Available",
    altered = "Yes",
    coverPhotoUrl = "google.com",
    currentLocation = LocationDto(tier1 = "The Rescue Ranch"),
    associatedPerson = AssociatedPersonDto(firstName = "Jane", lastName = "Doe"),
    lastIntakeUnixTime = 1767268800
)