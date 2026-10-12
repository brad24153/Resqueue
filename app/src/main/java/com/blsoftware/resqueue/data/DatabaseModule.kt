package com.blsoftware.resqueue.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.blsoftware.resqueue.data.local.AnimalDao
import com.blsoftware.resqueue.data.local.ResqueueDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ResqueueDatabase {
        return Room.databaseBuilder(
            context,
            ResqueueDatabase::class.java,
            "resqueue_database"
        ).build()
    }

    @Provides
    fun provideAnimalDao(database: ResqueueDatabase): AnimalDao {
        return database.animalDao()
    }
}