package com.example.practice.movies.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.practice.movies.DEFAULT_POSTER_URL
import com.example.practice.movies.presentation.MockData
import com.example.practice.movies.presentation.model.MovieListViewState
import com.example.practice.movies.presentation.model.MovieUiModel
import com.example.practice.movies.presentation.viewModel.MovieListViewModel
import com.example.practice.uikit.EmptyListMessage
import com.example.practice.uikit.FullscreenError
import com.example.practice.uikit.FullscreenLoading
import com.example.practice.uikit.Spacing
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.util.Locale

@Composable
fun MovieListScreen(
    favorites: Boolean = false,
) {
    val viewModel = koinViewModel<MovieListViewModel>() {
        parametersOf(favorites)
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(favorites) {
        if (favorites) {
            viewModel.refreshMovies()
        }
    }

    MovieListScreenContent(
        state,
        favorites,
        viewModel::onMovieClick,
        viewModel::onRetryClick,
        viewModel::onSettingsClick,
    )
}

@Composable
fun MovieListScreenContent(
    state: MovieListViewState,
    favorites: Boolean,
    onMovieClick: (MovieUiModel) -> Unit = {},
    onRetryClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
) {
    Scaffold(
        floatingActionButton = {
            if (!favorites)
                FloatingActionButton(onClick = onSettingsClick) {
                    BadgedBox(
                        badge = {
                            if (state.badgeCache.isBadgeActive())
                                Badge()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
        },
        contentWindowInsets = WindowInsets(0.dp),
    ) {
        Box(Modifier.padding(it)) {
            when (state.listState) {
                MovieListViewState.State.Loading -> {
                    FullscreenLoading()
                }

                is MovieListViewState.State.Error -> {
                    FullscreenError(
                        retry = onRetryClick,
                        text = state.listState.error
                    )
                }

                is MovieListViewState.State.Success -> {
                    if (state.listState.data.isEmpty()) {
                        EmptyListMessage()
                    } else {
                        LazyColumn {
                            state.listState.data.forEach { movie ->
                                item(key = movie.id) {
                                    MovieListItem(movie, onMovieClick)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieListItem(movie: MovieUiModel, onMovieClick: (MovieUiModel) -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onMovieClick(movie) }
            .padding(horizontal = Spacing.large)
            .padding(top = Spacing.medium)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = Spacing.large)
            ) {
                Text(
                    text = if (
                        !movie.name.isNullOrBlank() &&
                        !movie.alternativeName.isNullOrBlank()
                    ) {
                        "${movie.name} (${movie.alternativeName})"
                    } else {
                        movie.name ?: movie.alternativeName ?: "Без названия"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(vertical = Spacing.small)
                )
                Text(
                    text = movie.description ?: "Описание отсутствует",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .padding(top = Spacing.medium)
                        .padding(bottom = Spacing.big)
                ) {
                    movie.genres.forEach { genre ->
                        Card (
                            shape = RoundedCornerShape(Spacing.large),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        ) {
                            Text(
                                text = genre,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier
                                    .padding(horizontal = Spacing.medium, vertical = Spacing.small),
                            )
                        }
                    }
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = movie.posterUrl ?: DEFAULT_POSTER_URL,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = Spacing.medium)
                        .padding(bottom = Spacing.medium)
                        .width(80.dp)
                        .clip(RoundedCornerShape(Spacing.medium)),
                )

                Text(
                    text = String.format(Locale.US, "%.2f", movie.rating),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(bottom = Spacing.medium)
                )
            }
        }
        HorizontalDivider()
    }
}

@Preview(showBackground = true)
@Composable
fun MovieListPreview() {
    MovieListItem(
        MockData.getMovies().first(),
        {}
    )
}