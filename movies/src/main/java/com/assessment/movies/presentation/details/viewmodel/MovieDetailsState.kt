package com.assessment.movies.presentation.details.viewmodel

import com.assessment.movies.domain.model.details.MovieDetails

sealed interface MovieDetailsState {
    object Loading : MovieDetailsState
    data class Error(val message: String) : MovieDetailsState
    data class Content(val movieDetails: MovieDetails) : MovieDetailsState
}
