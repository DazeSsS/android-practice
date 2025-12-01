package com.example.movies.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movies.presentation.model.state.MovieSettingsState
import com.example.movies.presentation.viewModel.MovieSettingsViewModel
import com.example.uikit.uikit.Spacing
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieSettingsDialog() {
    val viewModel = koinViewModel<MovieSettingsViewModel>()

    val state by viewModel.state.collectAsStateWithLifecycle()

    MovieSettingsDialog(
        state,
        viewModel::onHighRatingFirstCheckedChange,
        viewModel::onBack,
        viewModel::onSaveClicked,
    )
}

@Composable
fun MovieSettingsDialog(
    state: MovieSettingsState,
    onHighRatingFirstCheckedChange: (Boolean) -> Unit = {},
    onBack: () -> Unit = {},
    onSaveClick: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onBack,
    ) {
        Column(
            Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(Spacing.big)
        ) {
            Text(
                text = "Настройки",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(Spacing.medium)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(
                    checked = state.highRatingFirst,
                    onCheckedChange = { onHighRatingFirstCheckedChange(it) }
                )

                Spacer(Modifier.width(Spacing.medium))

                Text("Сначала с высоким рейтингом")
            }

            TextButton(
                onClick = onSaveClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Сохранить")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieSettingsDialogPreview() {
    MovieSettingsDialog(MovieSettingsState())
}