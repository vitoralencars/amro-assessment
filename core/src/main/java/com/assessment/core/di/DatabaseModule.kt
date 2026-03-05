package com.assessment.core.di

import android.content.Context
import androidx.room.Room
import com.assessment.core.database.AmroDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AmroDatabase =
        Room.databaseBuilder(
            context,
            AmroDatabase::class.java,
            "amro_db",
        )
            .fallbackToDestructiveMigration(true)
            .build()

    @Provides
    fun provideGenreDao(db: AmroDatabase) = db.genreDao()
}
