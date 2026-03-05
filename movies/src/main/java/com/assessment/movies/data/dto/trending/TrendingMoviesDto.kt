package com.assessment.movies.data.dto.trending

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TrendingMoviesDto(
    val page: Int,
    val results: List<MovieItemDto>,
    @param:Json(name = "total_results")
    val totalResults: Int,
    @param:Json(name = "total_pages")
    val totalPages: Int,
)
