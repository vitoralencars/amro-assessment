package com.assessment.movies.domain.usecase

import androidx.paging.PagingData
import com.assessment.movies.domain.enums.MoviesSortingOption
import com.assessment.movies.domain.model.trending.MovieItem
import com.assessment.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTrendingMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
) {
    operator fun invoke(
        sortingOption: MoviesSortingOption,
        genreId: Int?,
    ): Flow<PagingData<MovieItem>> {
        return moviesRepository.getTrendingMovies(
            sortingOption = sortingOption,
            genreId = genreId ?: DEFAULT_GENRE_ID,
        )
    }

    private companion object {
        const val DEFAULT_GENRE_ID = -1
    }
}
