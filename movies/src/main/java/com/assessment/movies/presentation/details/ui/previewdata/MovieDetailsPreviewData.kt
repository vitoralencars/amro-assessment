package com.assessment.movies.presentation.details.ui.previewdata

import com.assessment.movies.domain.model.details.MovieDetails

internal val sampleMovie = MovieDetails(
    id = 1L,
    title = "Inception",
    overview = "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
    genres = listOf("Action", "Sci-Fi", "Adventure"),
    posterUrl = "https://image.tmdb.org/t/p/w500/8IB9SfsvYpsuEtpuS7pZ79fO7HG.jpg",
    voteAverage = 8.8,
    voteCount = 34500,
    revenue = 825532764,
    budget = 160000000,
    tagline = "Your mind is the scene of the crime.",
    imdbUrl = "https://www.imdb.com/title/tt1375666",
    runtime = 148,
    releaseDate = "2010-07-16",
    status = "Released"
)
