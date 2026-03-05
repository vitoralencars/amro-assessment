package com.assessment.movies.presentation

import com.assessment.core.network.NetworkResult
import com.assessment.movies.domain.model.details.MovieDetails
import com.assessment.movies.domain.usecase.GetMovieDetailsUseCase
import com.assessment.movies.presentation.details.viewmodel.MovieDetailsState
import com.assessment.movies.presentation.details.viewmodel.MovieDetailsViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsViewModelTest {

    private lateinit var viewModel: MovieDetailsViewModel
    @MockK
    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxed = true)
        viewModel = MovieDetailsViewModel(getMovieDetailsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getMovieDetails should update state to Content on success`() = runTest {
        // Given
        val movieId = 1L
        val movieDetails = mockk<MovieDetails>()
        coEvery { getMovieDetailsUseCase(movieId) } returns NetworkResult.Success(movieDetails)

        // When
        viewModel.getMovieDetails(movieId)

        // Then
        Assert.assertEquals(
            MovieDetailsState.Content(movieDetails),
            viewModel.state.value,
        )
    }

    @Test
    fun `getMovieDetails should update state to Error on failure`() = runTest {
        // Given
        val movieId = 1L
        val errorMessage = "Error message"
        coEvery { getMovieDetailsUseCase(movieId) } returns NetworkResult.Error(errorMessage)

        // When
        viewModel.getMovieDetails(movieId)

        // Then
        Assert.assertEquals(
            MovieDetailsState.Error(errorMessage),
            viewModel.state.value,
        )
    }
}
