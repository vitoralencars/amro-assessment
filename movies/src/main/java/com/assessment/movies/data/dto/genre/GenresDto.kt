package com.assessment.movies.data.dto.genre

import com.assessment.core.database.entity.GenreEntity

data class GenresDto(
    val genres: List<GenreDto>,
)

data class GenreDto(
    val id: Int,
    val name: String,
) {
    fun toEntity() = GenreEntity(
        id = id,
        name = name,
    )
}
