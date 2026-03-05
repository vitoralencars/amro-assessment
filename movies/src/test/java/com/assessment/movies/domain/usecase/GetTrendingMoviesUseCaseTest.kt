package com.assessment.movies.domain.usecase

import androidx.paging.PagingData
import com.assessment.movies.domain.enums.MoviesSortingOption
import com.assessment.movies.domain.model.trending.MovieItem
import com.assessment.movies.domain.repository.MoviesRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetTrendingMoviesUseCaseTest {

    private lateinit var getTrendingMoviesUseCase: GetTrendingMoviesUseCase
    @MockK
    private lateinit var repository: MoviesRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        getTrendingMoviesUseCase = GetTrendingMoviesUseCase(repository)
    }

    @Test
    fun `invoke should return flow of paging data from repository`() {
        // Given
        val sortingOption = MoviesSortingOption.POPULARITY
        val genreId = 1
        val pagingData = PagingData.empty<MovieItem>()
        val expectedFlow = flowOf(pagingData)
        
        every { 
            repository.getTrendingMovies(sortingOption, genreId) 
        } returns expectedFlow

        // When
        val result = getTrendingMoviesUseCase(sortingOption, genreId)

        // Then
        assertEquals(expectedFlow, result)
    }
}
