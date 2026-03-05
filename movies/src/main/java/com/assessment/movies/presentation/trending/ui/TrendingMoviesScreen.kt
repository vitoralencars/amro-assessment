package com.assessment.movies.presentation.trending.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.assessment.core.theme.AmroTheme
import com.assessment.movies.domain.model.trending.MovieItem
import com.assessment.movies.domain.enums.MoviesSortingOption
import com.assessment.movies.domain.model.trending.Genre
import com.assessment.movies.presentation.common.ui.ErrorView
import com.assessment.movies.presentation.trending.ui.previewdata.mockGenres
import com.assessment.movies.presentation.trending.ui.previewdata.mockMovies
import com.assessment.movies.presentation.trending.viewmodel.TrendingMoviesContentState
import com.assessment.movies.presentation.trending.viewmodel.TrendingMoviesViewModel
import kotlinx.coroutines.flow.flowOf

@Composable
fun TrendingMoviesScreen(
    onNavigateToMovieDetails: (movieId: Long) -> Unit,
    viewModel: TrendingMoviesViewModel = hiltViewModel(),
) {

    val contentState = viewModel.contentState.collectAsState()
    val movies = viewModel.trendingMoviesPagingData.collectAsLazyPagingItems()

    var firstLoadFinished by remember { mutableStateOf(false) }
    LaunchedEffect(movies.loadState.refresh) {
        if (movies.loadState.refresh is LoadState.NotLoading) {
            firstLoadFinished = true
        }
    }

    when (movies.loadState.refresh) {
        is LoadState.Loading -> LoadingView()
        is LoadState.Error -> ErrorView(
            onRetryClick = { movies.retry() },
        )
        is LoadState.NotLoading -> {
            ContentView(
                movieItems = movies,
                firstLoadFinished = firstLoadFinished,
                onMovieCardClick = onNavigateToMovieDetails,
                state = contentState.value,
                onSortingOptionSelected = { viewModel.onSortingOptionSelected(it) },
                onGenreSelected = { viewModel.onGenreSelected(it) },
            )
        }
    }
}

@Composable
private fun ContentView(
    movieItems: LazyPagingItems<MovieItem>,
    firstLoadFinished: Boolean,
    onMovieCardClick: (movieId: Long) -> Unit,
    state: TrendingMoviesContentState,
    onSortingOptionSelected: (MoviesSortingOption) -> Unit,
    onGenreSelected: (Genre) -> Unit,
) {
    TrendingMoviesContent(
        movieItems = movieItems,
        firstLoadFinished = firstLoadFinished,
        onMovieCardClick = onMovieCardClick,
        state = state,
        onSortingOptionSelected = onSortingOptionSelected,
        onGenreSelected = onGenreSelected,
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
private fun TrendingMoviesScreenContentPreview() {
    AmroTheme {

        val movies = flowOf(
            PagingData.from(mockMovies)
        ).collectAsLazyPagingItems()

        Surface(color = MaterialTheme.colorScheme.background) {
            TrendingMoviesContent(
                movieItems = movies,
                firstLoadFinished = true,
                onMovieCardClick = {},
                state = TrendingMoviesContentState(
                    availableGenres = mockGenres,
                ),
                onSortingOptionSelected = {},
                onGenreSelected = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun TrendingMoviesScreenEmptyPreview() {
    AmroTheme {

        val movies = flowOf(
            PagingData.from(emptyList<MovieItem>())
        ).collectAsLazyPagingItems()

        Surface(color = MaterialTheme.colorScheme.background) {
            TrendingMoviesContent(
                movieItems = movies,
                firstLoadFinished = true,
                onMovieCardClick = {},
                state = TrendingMoviesContentState(
                    availableGenres = mockGenres,
                ),
                onSortingOptionSelected = {},
                onGenreSelected = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun TrendingMoviesScreenLoadingPreview() {
    AmroTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            LoadingView()
        }
    }
}
