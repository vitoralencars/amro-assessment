package com.assessment.movies.data.dto.details

import com.assessment.movies.data.dto.genre.GenreDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDetailsDto(
    val id: Long,
    val title: String,
    val genres: List<GenreDto>,
    @param:Json(name = "backdrop_path")
    val backdropPath: String,
    @param:Json(name = "vote_average")
    val voteAverage: Double,
    @param:Json(name = "vote_count")
    val voteCount: Int,
    val revenue: Long,
    val budget: Long,
    val tagline: String,
    val overview: String,
    @param:Json(name = "imdb_id")
    val imdbId: String,
    val runtime: Int,
    @param:Json(name = "release_date")
    val releaseDate: String,
    val status: String,
)
