@file:OptIn(ExperimentalPagingApi::class)

package com.assessment.movies.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.assessment.core.database.AmroDatabase
import com.assessment.core.network.NetworkResult
import com.assessment.core.network.handleApiCall
import com.assessment.movies.data.mapper.MovieDetailsDtoToDomainMapper
import com.assessment.movies.data.mapper.TrendingMovieEntityToDomainMapper
import com.assessment.movies.data.remotemediator.TrendingMoviesRemoteMediator
import com.assessment.movies.data.service.MoviesApiService
import com.assessment.movies.domain.enums.MoviesSortingOption
import com.assessment.movies.domain.model.details.MovieDetails
import com.assessment.movies.domain.model.trending.Genre
import com.assessment.movies.domain.model.trending.MovieItem
import com.assessment.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val apiService: MoviesApiService,
    private val database: AmroDatabase,
    private val trendingMoviesRemoteMediator: TrendingMoviesRemoteMediator,
    private val trendingMovieEntityToDomainMapper: TrendingMovieEntityToDomainMapper,
    private val movieDetailsDtoToDomainMapper: MovieDetailsDtoToDomainMapper,
) : MoviesRepository {

    override fun getTrendingMovies(
        sortingOption: MoviesSortingOption,
        genreId: Int,
    ): Flow<PagingData<MovieItem>> {
        val dao = database.trendingMovieDao()

        val pagingSourceFactory = {
            when (sortingOption) {
                MoviesSortingOption.POPULARITY -> dao.pagingByPopularity(genreId)
                MoviesSortingOption.TITLE -> dao.pagingByTitle(genreId)
                MoviesSortingOption.RELEASE_DATE_DESC -> dao.pagingByReleaseDateDesc(genreId)
                MoviesSortingOption.RELEASE_DATE_ASC -> dao.pagingByReleaseDateAsc(genreId)
            }
        }

        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = trendingMoviesRemoteMediator,
            pagingSourceFactory = pagingSourceFactory,
        ).flow.map { pagingData -> pagingData.map {
            trendingMovieEntityToDomainMapper.transform(it)
        } }
    }

    override suspend fun getMovieDetails(movieId: Long): NetworkResult<MovieDetails> = handleApiCall {
        val response = apiService.getMovieDetails(movieId)
        movieDetailsDtoToDomainMapper.transform(dto = response)
    }

    override suspend fun getGenres(): List<Genre> {
        var localGenres = database.genreDao().getGenres()

        localGenres = localGenres.ifEmpty {
            val remoteGenres = apiService.getGenres()
            database.genreDao().upsertGenres(genres = remoteGenres.genres.map { it.toEntity() })
            database.genreDao().getGenres()
        }

        return localGenres.map { Genre(id = it.id, name = it.name) }
    }
}
