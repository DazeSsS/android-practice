package com.example.practice.movies.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.practice.R
import com.example.practice.movies.DEFAULT_POSTER_URL
import com.example.practice.movies.presentation.MockData
import com.example.practice.movies.presentation.model.MovieDetailViewState
import com.example.practice.movies.presentation.model.MovieUiModel
import com.example.practice.movies.presentation.viewModel.MovieDetailViewModel
import com.example.practice.uikit.Spacing
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movie: MovieUiModel,
) {
    val viewModel = koinViewModel<MovieDetailViewModel>() {
        parametersOf(movie)
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.movie_info_title)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.onBack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.onFavoriteChange() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            tint = if (state.isFavorite) Color.Black else Color.Gray,
                            contentDescription = null,
                        )
                    }
                }
            )
        }
    ) { padding ->
        MovieDetailContent(state, Modifier.padding(padding))
    }
}

@Composable
fun MovieDetailContent(
    state: MovieDetailViewState,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.large)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium),
    ) {
        AsyncImage(
            model = state.movie.posterUrl ?: DEFAULT_POSTER_URL,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Spacing.medium)
                .clip(RoundedCornerShape(Spacing.medium))
        )
        Text(
            text = if (
                !state.movie.name.isNullOrBlank() &&
                !state.movie.alternativeName.isNullOrBlank()
            ) {
                "${state.movie.name} (${state.movie.alternativeName})"
            } else {
                state.movie.name ?: state.movie.alternativeName ?: "Без названия"
            },
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = state.movie.description ?: "Описание отсутствует",
            style = MaterialTheme.typography.bodyMedium,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .padding(bottom = Spacing.medium)
        ) {
            state.movie.genres.forEach { genre ->
                Card (
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Text(
                        text = genre,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    )
                }
            }
        }
        HorizontalDivider()
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Рейтинг: ",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )

            Spacer(Modifier.width(Spacing.small))

            Card(
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    text = String.format(Locale.US, "%.2f", state.movie.rating),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = Spacing.medium),
                )
            }
        }

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )) {
                    append("Год выпуска: ")
                }
                append(state.movie.year.toString())
            }
        )

        val countryTitle = if (state.movie.isMultipleCountries) "Страны" else "Страна"
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )) {
                    append("$countryTitle: ")
                }
                append(
                    if (state.movie.isMultipleCountries) {
                        state.movie.countries.joinToString(", ") { it }
                    } else {
                        state.movie.countries.firstOrNull() ?: "Неизвестно"
                    }
                )
            },
            modifier = Modifier.padding(bottom = Spacing.large)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MovieDetailScreenPreview() {
    MovieDetailContent(
        MovieDetailViewState(MockData.getMovies().first()),
        Modifier
    )
}