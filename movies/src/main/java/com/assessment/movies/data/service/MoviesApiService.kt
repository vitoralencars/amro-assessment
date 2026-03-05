package com.assessment.movies.data.service

import com.assessment.movies.data.dto.details.MovieDetailsDto
import com.assessment.movies.data.dto.genre.GenresDto
import com.assessment.movies.data.dto.trending.TrendingMoviesDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApiService {

    @GET("trending/movie/week")
    suspend fun getTrendingMovies(
        @Query("page") page: Int,
    ): TrendingMoviesDto

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Long,
    ): MovieDetailsDto

    @GET("genre/movie/list")
    suspend fun getGenres(): GenresDto
}
