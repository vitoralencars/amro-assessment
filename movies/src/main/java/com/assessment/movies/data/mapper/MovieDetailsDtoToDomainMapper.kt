package com.assessment.movies.data.mapper

import com.assessment.movies.data.dto.details.MovieDetailsDto
import com.assessment.movies.domain.model.details.MovieDetails
import javax.inject.Inject

class MovieDetailsDtoToDomainMapper @Inject constructor() {

    fun transform(dto: MovieDetailsDto): MovieDetails =
        with(dto) {
            MovieDetails(
                id = id,
                title = title,
                overview = overview,
                genres = genres.map { it.name },
                posterUrl = "https://image.tmdb.org/t/p/w500$backdropPath",
                voteAverage = voteAverage,
                voteCount = voteCount,
                revenue = revenue,
                budget = budget,
                tagline = tagline,
                imdbUrl = "https://www.imdb.com/title/$imdbId",
                runtime = runtime,
                releaseDate = releaseDate,
                status = status,
            )
        }
}
