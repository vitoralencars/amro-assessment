package com.assessment.movies.presentation.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.assessment.core.theme.AmroTheme

@Composable
internal fun GenreTags(
    genres: List<String>,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier,
    ) {
        genres.forEach {
            GenreTag(it)
        }
    }
}

@Composable
private fun GenreTag(genre: String) {
    Text(
        text = genre,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = Modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .padding(8.dp)
    )
}

@PreviewLightDark
@Composable
fun GenreTagsPreview() {
    AmroTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            GenreTags(genres = listOf("Action", "Adventure", "Comedy"))
        }
    }
}
