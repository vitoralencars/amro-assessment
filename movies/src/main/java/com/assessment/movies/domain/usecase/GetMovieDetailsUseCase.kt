package com.assessment.movies.domain.usecase

import com.assessment.movies.domain.repository.MoviesRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MoviesRepository,
) {
    suspend operator fun invoke(movieId: Long) = repository.getMovieDetails(movieId)
}
