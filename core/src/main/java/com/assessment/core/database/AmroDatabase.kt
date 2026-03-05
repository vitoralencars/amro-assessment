package com.assessment.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.assessment.core.database.converter.GenreIdsConverter
import com.assessment.core.database.dao.GenreDao
import com.assessment.core.database.dao.TrendingMovieDao
import com.assessment.core.database.entity.GenreEntity
import com.assessment.core.database.entity.TrendingMovieEntity

@Database(
    entities = [
        GenreEntity::class,
        TrendingMovieEntity::class,
    ],
    version = 3,
    exportSchema = false,
)
@TypeConverters(GenreIdsConverter::class)
abstract class AmroDatabase : RoomDatabase() {
    abstract fun genreDao(): GenreDao

    abstract fun trendingMovieDao(): TrendingMovieDao
}
