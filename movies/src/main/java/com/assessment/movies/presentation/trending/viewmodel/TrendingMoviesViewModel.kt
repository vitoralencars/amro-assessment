@file:OptIn(ExperimentalCoroutinesApi::class)

package com.assessment.movies.presentation.trending.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.assessment.movies.domain.model.trending.MovieItem
import com.assessment.movies.domain.enums.MoviesSortingOption
import com.assessment.movies.domain.model.trending.Genre
import com.assessment.movies.domain.usecase.GetGenresUseCase
import com.assessment.movies.domain.usecase.GetTrendingMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TrendingMoviesViewModel @Inject constructor(
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
    private val getGenresUseCase: GetGenresUseCase,
) : ViewModel() {

    private val _contentState = MutableStateFlow(TrendingMoviesContentState())
    val contentState: StateFlow<TrendingMoviesContentState> = _contentState.asStateFlow()

    private val sortingOptionFlow = contentState.map { it.sortingOption }.distinctUntilChanged()

    private val selectedGenreFlow = contentState.map { it.genre }.distinctUntilChanged()

    private val genreMapFlow: StateFlow<Map<Int, String?>> = flow { emit(getGenresUseCase()) }
        .map { genres ->
            _contentState.update { it.copy(
                availableGenres = genres.filter { genre -> genre.name != null }
            ) }
            genres.associate { it.id to it.name }
        }
        .catch {
            emit(emptyMap())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyMap(),
        )

    val trendingMoviesPagingData: Flow<PagingData<MovieItem>> =
        combine(
            genreMapFlow,
            sortingOptionFlow,
            selectedGenreFlow,
        ) { map, sorting, selectedGenre ->
            Triple(map, sorting, selectedGenre)
        }.flatMapLatest { (map, sorting, selectedGenre) ->
            getTrendingMoviesUseCase(
                sortingOption = sorting,
                genreId = selectedGenre?.id,
            ).map { paging ->
                paging.map { movieItem ->
                    movieItem.copy(
                        genres = movieItem.genres.map { genre ->
                            genre.copy(name = map[genre.id])
                        }
                    )
                }
            }
        }.cachedIn(viewModelScope)

    fun onSortingOptionSelected(sortingOption: MoviesSortingOption) {
        _contentState.update { it.copy(sortingOption = sortingOption) }
    }

    fun onGenreSelected(genre: Genre) {
        _contentState.update { it.copy(genre = genre) }
    }
}
