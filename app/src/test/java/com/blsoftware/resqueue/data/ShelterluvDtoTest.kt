package com.blsoftware.resqueue.data

import com.blsoftware.resqueue.data.remote.AssociatedPersonDto
import com.blsoftware.resqueue.data.remote.LocationDto
import com.blsoftware.resqueue.data.remote.ShelterluvAnimalDto
import com.blsoftware.resqueue.data.remote.ShelterluvResponse
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/* Excluded from coverage report but left for testing to ensure JSON is parsed
   correctly. Will eventually be removed when R/W capability is implemented with
   either Firebase or AWS solution.
 */

class ShelterluvDtoTest {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Test
    fun deserializeJsonToShelterluvAnimalDto() {
        val jsonStr = """{
            "ID": "39127",
            "Name": "Heidi",
            "Breed": "Mixed Breed (Medium)",
            "Age": 46,
            "Sex": "Female",
            "Altered": "Yes",
            "CoverPhoto": "https://new-s3.shelterluv.com/profile-pictures/86bca0630306b5280fe9cb218bdbfde9/4e59e99bb7eb18179f2552f0e9d389fc.jpeg",
            "Status": "Available Foster",
            "CurrentLocation": {"Tier1": "The Rescue Ranch"},
            "AssociatedPerson": {
              "FirstName": "Jane",
              "LastName": "Doe"
            },
            "LastIntakeUnixTime": "1763600739"
        }"""

        val result = Json.decodeFromString<ShelterluvAnimalDto>(jsonStr)
        assertEquals("39127",result.id)
        assertEquals("Heidi",result.name)
        assertEquals("Mixed Breed (Medium)",result.breed)
        assertEquals(46,result.age)
        assertEquals("Female",result.gender)
        assertEquals("Yes",result.altered)
        assertEquals("https://new-s3.shelterluv.com/profile-pictures/86bca0630306b5280fe9cb218bdbfde9/4e59e99bb7eb18179f2552f0e9d389fc.jpeg",result.coverPhotoUrl)
        assertEquals("Available Foster",result.status)
        assertEquals("The Rescue Ranch",result.currentLocation?.tier1)
        assertEquals("Jane",result.associatedPerson?.firstName)
        assertEquals("Doe",result.associatedPerson?.lastName)
        assertEquals(1763600739L,result.lastIntakeUnixTime)
    }
}