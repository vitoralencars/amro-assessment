package com.assessment.movies.data.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator.MediatorResult
import androidx.room.withTransaction
import com.assessment.core.database.AmroDatabase
import com.assessment.core.database.dao.TrendingMovieDao
import com.assessment.core.database.entity.TrendingMovieEntity
import com.assessment.movies.data.dto.trending.MovieItemDto
import com.assessment.movies.data.dto.trending.TrendingMoviesDto
import com.assessment.movies.data.service.MoviesApiService
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalPagingApi::class)
class TrendingMoviesRemoteMediatorTest {

    private lateinit var remoteMediator: TrendingMoviesRemoteMediator
    @MockK
    private lateinit var apiService: MoviesApiService
    @MockK
    private lateinit var database: AmroDatabase
    @MockK
    private lateinit var trendingMovieDao: TrendingMovieDao

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)

        every { database.trendingMovieDao() } returns trendingMovieDao
        
        mockkStatic("androidx.room.RoomDatabaseKt")
        val transactionLambda = slot<suspend () -> MediatorResult>()
        coEvery { database.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }

        remoteMediator = TrendingMoviesRemoteMediator(
            apiService = apiService,
            database = database,
        )
    }

    @Test
    fun `refresh load should return Success when more data is present`() = runTest {
        // Given
        val moviesDto = createTrendingMoviesDto(
            results = listOf(createMovieItemDto(id = 1L))
        )
        val pagingState = createPagingState()
        coEvery { apiService.getTrendingMovies(page = 1) } returns moviesDto
        coEvery { trendingMovieDao.count() } returns 0 andThen 1
        coEvery { trendingMovieDao.clearAll() } just Runs
        coEvery { trendingMovieDao.upsertAll(any()) } just Runs

        // When
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        // Then
        assertTrue(result is MediatorResult.Success)
        assertFalse((result as MediatorResult.Success).endOfPaginationReached)
        coVerify { trendingMovieDao.clearAll() }
        coVerify { trendingMovieDao.upsertAll(any()) }
    }

    @Test
    fun `refresh load should return Success and end of pagination when no more data`() = runTest {
        // Given
        val moviesDto = createTrendingMoviesDto(results = emptyList())
        val pagingState = createPagingState()
        coEvery { apiService.getTrendingMovies(page = 1) } returns moviesDto
        coEvery { trendingMovieDao.count() } returns 0
        coEvery { trendingMovieDao.clearAll() } just Runs
        coEvery { trendingMovieDao.upsertAll(any()) } just Runs

        // When
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        // Then
        assertTrue(result is MediatorResult.Success)
        assertTrue((result as MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `append load should return Success and end of pagination when MAX_ITEMS reached`() = runTest {
        // Given
        coEvery { trendingMovieDao.count() } returns 100
        val pagingState = createPagingState()

        // When
        val result = remoteMediator.load(LoadType.APPEND, pagingState)

        // Then
        assertTrue(result is MediatorResult.Success)
        assertTrue((result as MediatorResult.Success).endOfPaginationReached)
        coVerify(exactly = 0) { apiService.getTrendingMovies(any()) }
    }

    @Test
    fun `append load should request next page correctly`() = runTest {
        // Given
        val pageSize = 20
        val loadedItems = 40
        val expectedPage = (loadedItems / pageSize) + 1 // 3
        
        coEvery { trendingMovieDao.count() } returns loadedItems
        val moviesDto = createTrendingMoviesDto(results = listOf(createMovieItemDto(id = 41L)))
        coEvery { apiService.getTrendingMovies(page = expectedPage) } returns moviesDto
        coEvery { trendingMovieDao.upsertAll(any()) } just Runs

        val pagingState = PagingState<Int, TrendingMovieEntity>(
            pages = listOf(
                PagingSource.LoadResult.Page(
                    data = List(loadedItems) { mockk() },
                    prevKey = null,
                    nextKey = 2
                )
            ),
            anchorPosition = null,
            config = PagingConfig(pageSize = pageSize),
            leadingPlaceholderCount = 0
        )

        // When
        val result = remoteMediator.load(LoadType.APPEND, pagingState)

        // Then
        assertTrue(result is MediatorResult.Success)
        coVerify { apiService.getTrendingMovies(page = expectedPage) }
    }

    @Test
    fun `prepend load should return Success and end of pagination reached`() = runTest {
        // Given
        coEvery { trendingMovieDao.count() } returns 10
        val pagingState = createPagingState()

        // When
        val result = remoteMediator.load(LoadType.PREPEND, pagingState)

        // Then
        assertTrue(result is MediatorResult.Success)
        assertTrue((result as MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `refresh load should return Error when API call fails and database is empty`() = runTest {
        // Given
        val exception = RuntimeException("Network Error")
        val pagingState = createPagingState()
        coEvery { apiService.getTrendingMovies(page = 1) } throws exception
        coEvery { trendingMovieDao.count() } returns 0

        // When
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        // Then
        assertTrue(result is MediatorResult.Error)
    }

    @Test
    fun `refresh load should return Success and end of pagination when API call fails but database is not empty`() = runTest {
        // Given
        val exception = RuntimeException("Network Error")
        val pagingState = createPagingState()
        coEvery { apiService.getTrendingMovies(page = 1) } throws exception
        coEvery { trendingMovieDao.count() } returns 10

        // When
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        // Then
        assertTrue(result is MediatorResult.Success)
        assertTrue((result as MediatorResult.Success).endOfPaginationReached)
    }

    private fun createPagingState() = PagingState<Int, TrendingMovieEntity>(
        pages = listOf(),
        anchorPosition = null,
        config = PagingConfig(pageSize = 20),
        leadingPlaceholderCount = 0
    )

    private fun createTrendingMoviesDto(
        page: Int = 1,
        results: List<MovieItemDto> = emptyList(),
        totalResults: Int = 1,
        totalPages: Int = 1
    ) = TrendingMoviesDto(
        page = page,
        results = results,
        totalResults = totalResults,
        totalPages = totalPages,
    )

    private fun createMovieItemDto(id: Long) = MovieItemDto(
        id = id,
        title = "Movie $id",
        genreIds = listOf(1, 2),
        posterPath = "/path$id",
        popularity = 100.0,
        releaseDate = "2026-10-10",
    )
}
