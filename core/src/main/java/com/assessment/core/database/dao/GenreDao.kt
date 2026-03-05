package com.assessment.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.assessment.core.database.entity.GenreEntity

@Dao
interface GenreDao {

    @Query("SELECT * FROM genres")
    suspend fun getGenres(): List<GenreEntity>

    @Upsert
    suspend fun upsertGenres(genres: List<GenreEntity>)
}
