package com.assessment.movies.domain.model.details

data class MovieDetails(
    val id: Long,
    val title: String,
    val overview: String,
    val genres: List<String>,
    val posterUrl: String,
    val voteAverage: Double,
    val voteCount: Int,
    val revenue: Long,
    val budget: Long,
    val tagline: String,
    val imdbUrl: String,
    val runtime: Int,
    val releaseDate: String,
    val status: String,
)
