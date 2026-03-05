package com.assessment.movies.domain.repository

import androidx.paging.PagingData
import com.assessment.core.network.NetworkResult
import com.assessment.movies.domain.enums.MoviesSortingOption
import com.assessment.movies.domain.model.details.MovieDetails
import com.assessment.movies.domain.model.trending.Genre
import com.assessment.movies.domain.model.trending.MovieItem
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun getTrendingMovies(
        sortingOption: MoviesSortingOption,
        genreId: Int,
    ): Flow<PagingData<MovieItem>>

    suspend fun getMovieDetails(movieId: Long): NetworkResult<MovieDetails>

    suspend fun getGenres(): List<Genre>
}
