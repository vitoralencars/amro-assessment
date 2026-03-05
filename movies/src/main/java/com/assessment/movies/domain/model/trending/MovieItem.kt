package com.assessment.movies.domain.model.trending

data class MovieItem(
    val id: Long,
    val title: String,
    val genres: List<Genre>,
    val posterUrl: String,
    val popularity: Double,
    val releaseDate: String,
)
