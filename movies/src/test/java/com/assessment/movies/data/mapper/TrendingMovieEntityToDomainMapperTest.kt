package com.assessment.movies.data.mapper

import com.assessment.core.database.entity.TrendingMovieEntity
import com.assessment.movies.domain.model.trending.Genre
import org.junit.Assert.assertEquals
import org.junit.Test

class TrendingMovieEntityToDomainMapperTest {

    private val mapper = TrendingMovieEntityToDomainMapper()

    @Test
    fun `transform should map entity to domain model correctly`() {
        // Given
        val entity = TrendingMovieEntity(
            id = 1L,
            title = "Movie Title",
            genreIds = listOf(1, 2),
            posterPath = "/path.jpg",
            popularity = 8.5,
            releaseDate = "2023-01-01"
        )

        // When
        val result = mapper.transform(entity)

        // Then
        assertEquals(entity.id, result.id)
        assertEquals(entity.title, result.title)
        assertEquals(entity.popularity, result.popularity, 0.0)
        assertEquals(entity.releaseDate, result.releaseDate)
        assertEquals("https://image.tmdb.org/t/p/w342/path.jpg", result.posterUrl)
        assertEquals(2, result.genres.size)
        assertEquals(Genre(1, null), result.genres[0])
        assertEquals(Genre(2, null), result.genres[1])
    }
}
