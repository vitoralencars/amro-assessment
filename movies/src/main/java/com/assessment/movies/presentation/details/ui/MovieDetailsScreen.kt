package com.assessment.movies.presentation.details.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.assessment.core.theme.AmroTheme
import com.assessment.movies.domain.model.details.MovieDetails
import com.assessment.movies.presentation.details.viewmodel.MovieDetailsState
import com.assessment.movies.presentation.details.viewmodel.MovieDetailsViewModel
import com.assessment.movies.presentation.common.ui.ErrorView
import com.assessment.movies.presentation.details.ui.previewdata.sampleMovie

@Composable
fun MovieDetailsScreen(
    movieId: Long,
    onBackClick: () -> Unit,
    viewModel: MovieDetailsViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.getMovieDetails(movieId)
    }

    when (val state = viewModel.state.collectAsState().value) {
        is MovieDetailsState.Loading -> LoadingView()
        is MovieDetailsState.Error -> ErrorView(
            onRetryClick = { viewModel.getMovieDetails(movieId) },
        )
        is MovieDetailsState.Content -> ContentView(
            movieDetails = state.movieDetails,
            onBackClick = onBackClick,
        )
    }
}

@Composable
private fun ContentView(
    movieDetails: MovieDetails,
    onBackClick: () -> Unit,
) {
    DetailsContent(
        movieDetails = movieDetails,
        onBackClick = onBackClick,
    )
}

@Composable
private fun LoadingView() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        CircularProgressIndicator()
    }
}

@PreviewLightDark
@Composable
fun MovieDetailsScreenContentPreview() {
    AmroTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ContentView(
                movieDetails = sampleMovie,
                onBackClick = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
fun MovieDetailsScreenLoadingPreview() {
    AmroTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            LoadingView()
        }
    }
}
