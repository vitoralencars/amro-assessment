package com.assessment.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("trending_movies")
data class TrendingMovieEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val genreIds: List<Int>,
    val posterPath: String,
    val popularity: Double,
    val releaseDate: String,
)
