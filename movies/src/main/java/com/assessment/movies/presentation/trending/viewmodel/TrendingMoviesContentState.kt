package com.assessment.movies.presentation.trending.viewmodel

import com.assessment.movies.domain.enums.MoviesSortingOption
import com.assessment.movies.domain.model.trending.Genre

data class TrendingMoviesContentState (
    val availableGenres: List<Genre> = emptyList(),
    val genre: Genre? = null,
    val sortingOption: MoviesSortingOption = MoviesSortingOption.POPULARITY,
)
