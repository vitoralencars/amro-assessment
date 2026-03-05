package com.assessment.movies.data.dto.trending

import com.assessment.core.database.entity.TrendingMovieEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieItemDto(
    val id: Long,
    val title: String,
    @param:Json(name = "genre_ids")
    val genreIds: List<Int>,
    @param:Json(name = "poster_path")
    val posterPath: String,
    val popularity: Double,
    @param:Json(name = "release_date")
    val releaseDate: String,
) {
    fun toEntity() = TrendingMovieEntity(
        id = id,
        title = title,
        genreIds = genreIds,
        posterPath = posterPath,
        popularity = popularity,
        releaseDate = releaseDate,
    )
}
