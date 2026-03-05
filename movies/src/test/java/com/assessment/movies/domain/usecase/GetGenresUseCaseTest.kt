package com.assessment.movies.domain.usecase

import com.assessment.movies.domain.model.trending.Genre
import com.assessment.movies.domain.repository.MoviesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetGenresUseCaseTest {

    private lateinit var getGenresUseCase: GetGenresUseCase
    @MockK
    private lateinit var repository: MoviesRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        getGenresUseCase = GetGenresUseCase(repository)
    }

    @Test
    fun `invoke should return list of genres from repository`() = runTest {
        // Given
        val genres = listOf(Genre(1, "Action"), Genre(2, "Comedy"))
        coEvery { repository.getGenres() } returns genres

        // When
        val result = getGenresUseCase()

        // Then
        assertEquals(genres, result)
    }
}
