package com.assessment.movies.data.mapper

import com.assessment.core.database.entity.TrendingMovieEntity
import com.assessment.movies.domain.model.trending.Genre
import com.assessment.movies.domain.model.trending.MovieItem
import javax.inject.Inject

class TrendingMovieEntityToDomainMapper @Inject constructor() {
    fun transform(entity: TrendingMovieEntity): MovieItem =
        with(entity) {
            MovieItem(
                id = id,
                title = title,
                genres = genreIds.map { genreId -> Genre(id = genreId, name = null) },
                posterUrl = "https://image.tmdb.org/t/p/w342$posterPath",
                popularity = popularity,
                releaseDate = releaseDate,
            )
        }
}
