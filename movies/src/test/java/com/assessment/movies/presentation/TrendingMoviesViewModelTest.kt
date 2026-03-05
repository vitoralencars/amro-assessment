package com.assessment.movies.presentation

import androidx.paging.PagingData
import com.assessment.movies.domain.enums.MoviesSortingOption
import com.assessment.movies.domain.model.trending.Genre
import com.assessment.movies.domain.model.trending.MovieItem
import com.assessment.movies.domain.usecase.GetGenresUseCase
import com.assessment.movies.domain.usecase.GetTrendingMoviesUseCase
import com.assessment.movies.presentation.trending.viewmodel.TrendingMoviesViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TrendingMoviesViewModelTest {

    private lateinit var viewModel: TrendingMoviesViewModel
    @MockK
    private lateinit var getTrendingMoviesUseCase: GetTrendingMoviesUseCase
    @MockK
    private lateinit var getGenresUseCase: GetGenresUseCase
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxed = true)

        coEvery { getGenresUseCase() } returns listOf(
            Genre(1, "Action"),
            Genre(2, "Comedy"),
        )
        every {
            getTrendingMoviesUseCase(
                any(),
                any(),
            )
        } returns flowOf(PagingData.empty())

        viewModel = TrendingMoviesViewModel(getTrendingMoviesUseCase, getGenresUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onSortingOptionSelected should update contentState`() = runTest {
        // When
        viewModel.onSortingOptionSelected(MoviesSortingOption.TITLE)

        // Then
        Assert.assertEquals(
            MoviesSortingOption.TITLE,
            viewModel.contentState.value.sortingOption,
        )
    }

    @Test
    fun `onGenreSelected should update contentState`() = runTest {
        // Given
        val genre = Genre(1, "Action")

        // When
        viewModel.onGenreSelected(genre)

        // Then
        Assert.assertEquals(genre, viewModel.contentState.value.genre)
    }

    @Test
    fun `trendingMoviesPagingData should emit data when state changes`() = runTest {
        // Given
        val pagingData = PagingData.from(listOf(mockk<MovieItem>(relaxed = true)))
        every {
            getTrendingMoviesUseCase(any(), any())
        } returns flowOf(pagingData)

        // When
        viewModel.trendingMoviesPagingData.first()

        // Then
        verify { getTrendingMoviesUseCase(any(), any()) }
    }
}
