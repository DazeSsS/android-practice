package com.example.profile.presentation.screen

import android.app.DownloadManager
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.profile.R
import com.example.profile.presentation.model.state.ProfileState
import com.example.core.core.SystemBroadcastReceiver
import com.example.profile.presentation.viewModel.ProfileViewModel
import com.example.uikit.theme.Typography
import com.example.uikit.uikit.Spacing
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    val viewModel = koinViewModel<ProfileViewModel>()

    val state = viewModel.state

    val context = LocalContext.current

    val DOWNLOAD_COMPLETE_ACTION = "android.intent.action.DOWNLOAD_COMPLETE"

    SystemBroadcastReceiver(
        systemAction = DOWNLOAD_COMPLETE_ACTION,
        onSystemEvent = { intent ->
            if (intent?.action == DOWNLOAD_COMPLETE_ACTION) {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
                if (id != -1L) {
                    viewModel.navigateToDownloadInvoice(context)
                }
            }
        }
    )

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.profile_title))
                },
                actions = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        Modifier
                            .padding(end = Spacing.medium)
                            .clickable { viewModel.onEditClick() }
                    )
                }
            )
        }
    ) { padding ->
        ProfileContent(
            modifier = Modifier.padding(padding),
            context = context,
            state = state,
            enqueueDownloadRequest = viewModel::enqueueDownloadRequest,
        )
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    context: Context,
    state: ProfileState,
    enqueueDownloadRequest: (String, Context) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = state.photoUri,
            modifier = Modifier
                .padding(bottom = Spacing.medium)
                .clip(CircleShape)
                .size(128.dp),
            contentDescription = "photo",
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.profile)
        )

        Text(
            text = state.name,
            style = Typography.headlineLarge
        )

        Button(
            onClick = {
                if (state.url.isNotBlank()) {
                    enqueueDownloadRequest(
                        state.url,
                        context
                    )
                }
            }
        ) {
            Text(text = stringResource(R.string.resume_button))
        }
    }
}
