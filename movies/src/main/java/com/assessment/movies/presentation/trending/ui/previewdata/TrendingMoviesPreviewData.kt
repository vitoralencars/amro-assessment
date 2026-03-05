package com.assessment.movies.presentation.trending.ui.previewdata

import com.assessment.movies.domain.model.trending.Genre
import com.assessment.movies.domain.model.trending.MovieItem

internal val mockGenres = listOf(
    Genre(id = 1, name = "Action"),
    Genre(id = 2, name = "Adventure"),
    Genre(id = 3, name = "Comedy"),
)

internal val mockMovies = listOf(
    MovieItem(
        id = 1,
        title = "Inception",
        genres = listOf(mockGenres[0], mockGenres[1]),
        posterUrl = "https://example.com/inception.jpg",
        popularity = 9.0,
        releaseDate = "2010-07-16"
    ),
    MovieItem(
        id = 2,
        title = "The Dark Knight",
        genres = listOf(mockGenres[0]),
        posterUrl = "https://example.com/tdk.jpg",
        popularity = 9.5,
        releaseDate = "2008-07-18"
    )
)
