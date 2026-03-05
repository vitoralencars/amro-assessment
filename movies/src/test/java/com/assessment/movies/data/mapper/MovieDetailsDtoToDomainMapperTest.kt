package com.assessment.movies.data.mapper

import com.assessment.movies.data.dto.details.MovieDetailsDto
import com.assessment.movies.data.dto.genre.GenreDto
import org.junit.Assert.assertEquals
import org.junit.Test

class MovieDetailsDtoToDomainMapperTest {

    private val mapper = MovieDetailsDtoToDomainMapper()

    @Test
    fun `transform should map dto to domain model correctly`() {
        // Given
        val dto = MovieDetailsDto(
            id = 1L,
            title = "Movie Title",
            overview = "Overview text",
            genres = listOf(
                GenreDto(1, "Action"),
                GenreDto(2, "Comedy"),
            ),
            backdropPath = "/backdrop.jpg",
            voteAverage = 8.5,
            voteCount = 1000,
            revenue = 500000000,
            budget = 100000000,
            tagline = "Great movie",
            imdbId = "tt1234567",
            runtime = 120,
            releaseDate = "2023-01-01",
            status = "Released",
        )

        // When
        val result = mapper.transform(dto)

        // Then
        assertEquals(dto.id, result.id)
        assertEquals(dto.title, result.title)
        assertEquals(dto.overview, result.overview)
        assertEquals(listOf("Action", "Comedy"), result.genres)
        assertEquals("https://image.tmdb.org/t/p/w500/backdrop.jpg", result.posterUrl)
        assertEquals(dto.voteAverage, result.voteAverage, 0.0)
        assertEquals(dto.voteCount, result.voteCount)
        assertEquals(dto.revenue, result.revenue)
        assertEquals(dto.budget, result.budget)
        assertEquals(dto.tagline, result.tagline)
        assertEquals("https://www.imdb.com/title/tt1234567", result.imdbUrl)
        assertEquals(dto.runtime, result.runtime)
        assertEquals(dto.releaseDate, result.releaseDate)
        assertEquals(dto.status, result.status)
    }
}
