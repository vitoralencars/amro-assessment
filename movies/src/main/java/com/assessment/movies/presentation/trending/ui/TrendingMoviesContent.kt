@file:OptIn(ExperimentalMaterial3Api::class)

package com.assessment.movies.presentation.trending.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MovieFilter
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import coil3.compose.AsyncImage
import com.assessment.movies.R
import com.assessment.movies.domain.model.trending.MovieItem
import com.assessment.movies.presentation.common.ui.GenreTags
import com.assessment.movies.domain.enums.MoviesSortingOption
import com.assessment.movies.domain.model.trending.Genre
import com.assessment.movies.presentation.trending.viewmodel.TrendingMoviesContentState
import kotlinx.coroutines.launch

@Composable
internal fun TrendingMoviesContent(
    movieItems: LazyPagingItems<MovieItem>,
    firstLoadFinished: Boolean,
    onMovieCardClick: (movieId: Long) -> Unit,
    state: TrendingMoviesContentState,
    onSortingOptionSelected: (MoviesSortingOption) -> Unit,
    onGenreSelected: (Genre) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val availableGenres = listOf(Genre(
        id = -1,
        name = stringResource(R.string.all_genre_name),
    )) + state.availableGenres

    var showSortingBottomSheet by remember { mutableStateOf(false) }
    var showGenreFilterBottomSheet by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            SortingBar(
                selectedItem = state.sortingOption.label,
                onClick = { showSortingBottomSheet = true },
            )
            GenreFilterBar(
                selectedItem = state.genre?.name ?: availableGenres[0].name.orEmpty(),
                onClick = { showGenreFilterBottomSheet = true },
            )
        }

        when {
            firstLoadFinished && movieItems.itemCount == 0 -> {
                EmptyView()
            }
            else -> {
                MoviesList(
                    movieItems = movieItems,
                    onMovieCardClick = onMovieCardClick,
                )
            }
        }
    }

    if (showSortingBottomSheet) {
        SortingBottomSheet(
            sheetState = sheetState,
            currentSelectedIndex = MoviesSortingOption.entries.indexOf(state.sortingOption),
            onItemSelected = { sortingOption ->
                onSortingOptionSelected(sortingOption)
            },
            onDismissRequest = {
                showSortingBottomSheet = false
                scope.launch { sheetState.hide() }
            },
        )
    }

    if (showGenreFilterBottomSheet) {
        GenreFilterBottomSheet(
            sheetState = sheetState,
            currentSelectedIndex = if (state.genre != null) {
                availableGenres.indexOf(state.genre)
            } else {
                0
            },
            genres = availableGenres.mapNotNull { it.name },
            onItemSelectedIndex = { index ->
                onGenreSelected(availableGenres[index])
            },
            onDismissRequest = {
                showGenreFilterBottomSheet = false
                scope.launch { sheetState.hide() }
            },
        )
    }
}

@Composable
private fun MoviesList(
    movieItems: LazyPagingItems<MovieItem>,
    onMovieCardClick: (movieId: Long) -> Unit,
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(
            count = movieItems.itemCount,
            key = { index ->
                "key_$index"
            },
        ) { index ->
            movieItems[index]?.let { movieItem ->
                MovieCard(
                    movieItem = movieItem,
                    onClick = {
                        onMovieCardClick(movieItem.id)
                    },
                )
            }
        }
    }
}

@Composable
private fun RowScope.SortingBar(
    selectedItem: String,
    onClick: () -> Unit,
) {
    BarContainer(
        selectedItem = selectedItem,
        decorationIcon = Icons.Default.SwapVert,
        onClick = onClick,
        modifier = Modifier.weight(1f),
    )
}

@Composable
private fun RowScope.GenreFilterBar(
    selectedItem: String,
    onClick: () -> Unit,
) {
    BarContainer(
        selectedItem = selectedItem,
        decorationIcon = Icons.Default.MovieFilter,
        onClick = onClick,
        modifier = Modifier.weight(1f),
    )
}

@Composable
private fun BarContainer(
    selectedItem: String,
    decorationIcon: ImageVector,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .clickable { onClick() }
            .padding(8.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Icon(
                imageVector = decorationIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            Text(
                text = selectedItem,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    }
}

@Composable
private fun MovieCard(
    movieItem: MovieItem,
    onClick: () -> Unit,
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(12.dp))
                .clickable { onClick() }
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp),
        ) {
            AsyncImage(
                model = movieItem.posterUrl,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(12.dp)),
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                Text(
                    text = movieItem.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                GenreTags(genres = movieItem.genres.mapNotNull { it.name })
            }
        }
    }
}

@Composable
private fun SortingBottomSheet(
    sheetState: SheetState,
    currentSelectedIndex: Int,
    onItemSelected: (MoviesSortingOption) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val sortingOptions = MoviesSortingOption.entries

    SelectionBottomSheet(
        sheetState = sheetState,
        sectionTitle = stringResource(R.string.sorting_movies_section),
        optionsList = sortingOptions.map { it.label },
        currentSelectedIndex = currentSelectedIndex,
        onItemSelectedIndex = { index ->
            onItemSelected(sortingOptions[index])
        },
        onDismissRequest = onDismissRequest,
    )
}

@Composable
private fun GenreFilterBottomSheet(
    sheetState: SheetState,
    currentSelectedIndex: Int,
    genres: List<String>,
    onItemSelectedIndex: (Int) -> Unit,
    onDismissRequest: () -> Unit,
) {
    SelectionBottomSheet(
        sheetState = sheetState,
        sectionTitle = stringResource(R.string.available_genres_section),
        optionsList = genres,
        currentSelectedIndex = currentSelectedIndex,
        onItemSelectedIndex = onItemSelectedIndex,
        onDismissRequest = onDismissRequest,
    )
}

@Composable
private fun SelectionBottomSheet(
    sheetState: SheetState,
    sectionTitle: String,
    optionsList: List<String>,
    currentSelectedIndex: Int,
    onItemSelectedIndex: (Int) -> Unit,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = sectionTitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(16.dp),
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
            ) {
                itemsIndexed(optionsList) { index, item ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onItemSelectedIndex(index)
                                    onDismissRequest()
                                }
                        ) {
                            RadioButton(
                                selected = index == currentSelectedIndex,
                                onClick = {
                                    onItemSelectedIndex(index)
                                    onDismissRequest()
                                },
                            )
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(16.dp),
                            )
                        }
                        if (index != optionsList.lastIndex) {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyView() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = stringResource(R.string.movies_list_empty_state),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}
