package com.example.practice.movies.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.practice.movies.DEFAULT_POSTER_URL
import com.example.practice.movies.presentation.MockData
import com.example.practice.movies.presentation.model.MovieUiModel
import com.example.practice.movies.presentation.viewModel.MovieListViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MovieListScreen() {
    val viewModel = koinViewModel<MovieListViewModel>() {
        parametersOf(MockData.getMovies())
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    LazyColumn {
        state.movies.forEach { movie ->
            item(key = movie.id) {
                MovieListItem(movie) { viewModel.showMovieDetail(it) }
            }
        }
    }
}

@Composable
fun MovieListItem(movie: MovieUiModel, onMovieClick: (MovieUiModel) -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onMovieClick(movie) }
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
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
                        .padding(vertical = 4.dp)
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
                        .padding(top = 8.dp)
                        .padding(bottom = 12.dp)
                ) {
                    movie.genres.forEach { genre ->
                        Card (
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        ) {
                            Text(
                                text = genre.name,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            )
                        }
                    }
                }
            }
            AsyncImage(
                model = movie.posterUrl ?: DEFAULT_POSTER_URL,
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(bottom = 16.dp)
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
        HorizontalDivider()
    }
}

@Preview(showBackground = true)
@Composable
fun MovieListPreview() {
    MovieListScreen()
}