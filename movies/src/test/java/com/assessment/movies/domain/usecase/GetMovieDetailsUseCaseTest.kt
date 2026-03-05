package com.assessment.movies.domain.usecase

import com.assessment.core.network.NetworkResult
import com.assessment.movies.domain.model.details.MovieDetails
import com.assessment.movies.domain.repository.MoviesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetMovieDetailsUseCaseTest {

    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase
    @MockK
    private lateinit var repository: MoviesRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        getMovieDetailsUseCase = GetMovieDetailsUseCase(repository)
    }

    @Test
    fun `invoke should return success result from repository`() = runTest {
        // Given
        val movieId = 1L
        val movieDetails = MovieDetails(
            id = movieId,
            title = "Movie Title",
            overview = "Overview",
            genres = listOf("Action"),
            posterUrl = "url",
            voteAverage = 8.0,
            voteCount = 100,
            revenue = 1000000,
            budget = 500000,
            tagline = "Tagline",
            imdbUrl = "imdb",
            runtime = 120,
            releaseDate = "2023-01-01",
            status = "Released"
        )
        val expectedResult = NetworkResult.Success(movieDetails)
        coEvery { repository.getMovieDetails(movieId) } returns expectedResult

        // When
        val result = getMovieDetailsUseCase(movieId)

        // Then
        assertEquals(expectedResult, result)
    }

    @Test
    fun `invoke should return error result from repository`() = runTest {
        // Given
        val movieId = 1L
        val expectedResult = NetworkResult.Error("Error message")
        coEvery { repository.getMovieDetails(movieId) } returns expectedResult

        // When
        val result = getMovieDetailsUseCase(movieId)

        // Then
        assertEquals(expectedResult, result)
    }
}
