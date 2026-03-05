package com.assessment.movies.presentation.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assessment.core.network.NetworkResult
import com.assessment.movies.domain.usecase.GetMovieDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<MovieDetailsState>(MovieDetailsState.Loading)
    val state: StateFlow<MovieDetailsState> = _state

    fun getMovieDetails(movieId: Long) {
        viewModelScope.launch {
            when (val result = getMovieDetailsUseCase(movieId)) {
                is NetworkResult.Success -> {
                    _state.update { MovieDetailsState.Content(movieDetails = result.data) }
                }
                is NetworkResult.Error -> {
                    _state.update { MovieDetailsState.Error(message = result.message) }
                }
            }
        }
    }
}
