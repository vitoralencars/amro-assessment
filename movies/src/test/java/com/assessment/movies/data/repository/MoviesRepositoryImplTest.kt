package com.assessment.movies.data.repository

import androidx.paging.PagingSource
import com.assessment.core.database.AmroDatabase
import com.assessment.core.database.dao.GenreDao
import com.assessment.core.database.dao.TrendingMovieDao
import com.assessment.core.database.entity.GenreEntity
import com.assessment.core.database.entity.TrendingMovieEntity
import com.assessment.core.network.NetworkResult
import com.assessment.movies.data.dto.details.MovieDetailsDto
import com.assessment.movies.data.dto.genre.GenreDto
import com.assessment.movies.data.dto.genre.GenresDto
import com.assessment.movies.data.mapper.MovieDetailsDtoToDomainMapper
import com.assessment.movies.data.mapper.TrendingMovieEntityToDomainMapper
import com.assessment.movies.data.remotemediator.TrendingMoviesRemoteMediator
import com.assessment.movies.data.service.MoviesApiService
import com.assessment.movies.domain.enums.MoviesSortingOption
import com.assessment.movies.domain.model.details.MovieDetails
import com.assessment.movies.domain.model.trending.Genre
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MoviesRepositoryImplTest {

    private lateinit var repository: MoviesRepositoryImpl
    @MockK
    private lateinit var apiService: MoviesApiService
    @MockK
    private lateinit var database: AmroDatabase
    @MockK
    private lateinit var genreDao: GenreDao
    @MockK
    private lateinit var trendingMovieDao: TrendingMovieDao
    @MockK
    private lateinit var trendingMoviesRemoteMediator: TrendingMoviesRemoteMediator
    @MockK
    private lateinit var trendingMovieEntityToDomainMapper: TrendingMovieEntityToDomainMapper
    @MockK
    private lateinit var movieDetailsDtoToDomainMapper: MovieDetailsDtoToDomainMapper

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        every { database.genreDao() } returns genreDao
        every { database.trendingMovieDao() } returns trendingMovieDao

        repository = MoviesRepositoryImpl(
            apiService,
            database,
            trendingMoviesRemoteMediator,
            trendingMovieEntityToDomainMapper,
            movieDetailsDtoToDomainMapper
        )
    }

    @Test
    fun `getMovieDetails should return success when api call is successful`() = runTest {
        // Given
        val movieId = 1L
        val dto = mockk<MovieDetailsDto>()
        val domain = mockk<MovieDetails>()
        coEvery { apiService.getMovieDetails(movieId) } returns dto
        every { movieDetailsDtoToDomainMapper.transform(dto) } returns domain

        // When
        val result = repository.getMovieDetails(movieId)

        // Then
        assertEquals(NetworkResult.Success(domain), result)
    }

    @Test
    fun `getMovieDetails should return error when api call fails`() = runTest {
        // Given
        val movieId = 1L
        val errorMessage = "Network Error"
        coEvery { apiService.getMovieDetails(movieId) } throws Exception(errorMessage)

        // When
        val result = repository.getMovieDetails(movieId)

        // Then
        assertEquals(NetworkResult.Error(errorMessage), result)
    }

    @Test
    fun `getGenres should return local genres if not empty`() = runTest {
        // Given
        val localEntities = listOf(GenreEntity(1, "Action"))
        val expectedGenres = listOf(Genre(1, "Action"))
        coEvery { genreDao.getGenres() } returns localEntities

        // When
        val result = repository.getGenres()

        // Then
        assertEquals(expectedGenres, result)
        coVerify(exactly = 0) { apiService.getGenres() }
    }

    @Test
    fun `getGenres should fetch remote genres and save to database if local is empty`() = runTest {
        // Given
        val remoteDto = GenresDto(listOf(GenreDto(1, "Action")))
        val localEntities = listOf(GenreEntity(1, "Action"))
        val expectedGenres = listOf(Genre(1, "Action"))

        coEvery { genreDao.getGenres() } returnsMany listOf(emptyList(), localEntities)
        coEvery { apiService.getGenres() } returns remoteDto
        coEvery { genreDao.upsertGenres(any()) } returns Unit

        // When
        val result = repository.getGenres()

        // Then
        assertEquals(expectedGenres, result)
        coVerify(exactly = 1) { apiService.getGenres() }
        coVerify(exactly = 1) { genreDao.upsertGenres(any()) }
    }

    @Test
    fun `getTrendingMovies with POPULARITY should call pagingByPopularity`() = runTest {
        // Given
        val mockPagingSource = mockk<PagingSource<Int, TrendingMovieEntity>>(relaxed = true)
        every { trendingMovieDao.pagingByPopularity(1) } returns mockPagingSource

        // When
        repository.getTrendingMovies(
            sortingOption = MoviesSortingOption.POPULARITY,
            genreId = 1,
        ).first()

        // Then
        verify { trendingMovieDao.pagingByPopularity(1) }
    }

    @Test
    fun `getTrendingMovies with TITLE should call pagingByTitle`() = runTest {
        // Given
        val mockPagingSource = mockk<PagingSource<Int, TrendingMovieEntity>>(relaxed = true)
        every { trendingMovieDao.pagingByTitle(1) } returns mockPagingSource

        // When
        repository.getTrendingMovies(
            sortingOption = MoviesSortingOption.TITLE,
            genreId = 1,
        ).first()

        // Then
        verify { trendingMovieDao.pagingByTitle(1) }
    }

    @Test
    fun `getTrendingMovies with RELEASE_DATE_DESC should call pagingByReleaseDateDesc`() = runTest {
        // Given
        val mockPagingSource = mockk<PagingSource<Int, TrendingMovieEntity>>(relaxed = true)
        every { trendingMovieDao.pagingByReleaseDateDesc(1) } returns mockPagingSource

        // When
        repository.getTrendingMovies(
            sortingOption = MoviesSortingOption.RELEASE_DATE_DESC,
            genreId = 1,
        ).first()

        // Then
        verify { trendingMovieDao.pagingByReleaseDateDesc(1) }
    }

    @Test
    fun `getTrendingMovies with RELEASE_DATE_ASC should call pagingByReleaseDateAsc`() = runTest {
        // Given
        val mockPagingSource = mockk<PagingSource<Int, TrendingMovieEntity>>(relaxed = true)
        every { trendingMovieDao.pagingByReleaseDateAsc(1) } returns mockPagingSource

        // When
        repository.getTrendingMovies(
            sortingOption = MoviesSortingOption.RELEASE_DATE_ASC,
            genreId = 1,
        ).first()

        // Then
        verify { trendingMovieDao.pagingByReleaseDateAsc(1) }
    }
}
