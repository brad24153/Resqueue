package com.blsoftware.resqueue.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [AnimalEntity::class], version = 1, exportSchema = false)
abstract class ResqueueDatabase : RoomDatabase() {
    abstract fun animalDao(): AnimalDao

}