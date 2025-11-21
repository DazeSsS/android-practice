package com.example.practice.profile.presentation.screen

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.practice.R
import com.example.practice.profile.presentation.model.state.EditProfileState
import com.example.practice.profile.presentation.viewModel.EditProfileViewModel
import com.example.practice.uikit.Spacing
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen() {
    val viewModel = koinViewModel<EditProfileViewModel>()

    val state = viewModel.state

    val context = LocalContext.current

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val pickMedia: ActivityResultLauncher<PickVisualMediaRequest> =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            viewModel.onImageSelect(uri)
        }

    val mGetContent = rememberLauncherForActivityResult<Uri, Boolean>(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            viewModel.onImageSelect(imageUri)
        }
    }

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (!isGranted) {
                val dialog = AlertDialog.Builder(context)
                    .setMessage(
                        "Для редактирования профиля предоставьте доступ к файлам.\n\n" +
                        "Вы можете выдать разрешение позже в настройках."
                    )
                    .setCancelable(false)
                    .setPositiveButton("OK") { _, _ ->
                        viewModel.onBack()
                    }

                dialog.show()
            }
            viewModel.onPermissionClose()
        }

    fun onCameraSelect() {
        val baseDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        val pictureFile = File(baseDir, "picture_${Date().time}.jpg")
        imageUri = FileProvider.getUriForFile(
            context,
            context.packageName + ".provider",
            pictureFile
        )
        imageUri?.let { mGetContent.launch(it) }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.edit_profile_title))
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        Modifier
                            .padding(end = Spacing.medium)
                            .clickable { viewModel.onBack() }
                    )
                },
                actions = {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = null,
                        Modifier
                            .padding(end = Spacing.medium)
                            .clickable {
                                viewModel.onDoneClick()
                            }
                    )
                },
                modifier = Modifier.shadow(elevation = 1.dp)
            )
        }
    ) { padding ->
        EditProfileContent(
            modifier = Modifier.padding(padding),
            state = state,
            onAvatarClick = viewModel::onAvatarClick,
            onNameChange = { name -> viewModel.onNameChange(name) },
            onUrlChange = { url -> viewModel.onUrlChange(url) },
        )
    }

    if (state.isNeedToShowPermission) {
        LaunchedEffect(Unit) {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }
        }
    }

    if (state.isNeedToShowSelect) {
        Dialog(onDismissRequest = { viewModel.onSelectDismiss() }) {
            Surface(
                modifier = Modifier.fillMaxWidth(0.8f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = stringResource(R.string.camera_choice),
                        Modifier.clickable {
                            onCameraSelect()
                            viewModel.onSelectDismiss()
                        }
                    )
                    Text(
                        text = stringResource(R.string.gallery_choice),
                        Modifier.clickable {
                            pickMedia.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                            viewModel.onSelectDismiss()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EditProfileContent(
    modifier: Modifier = Modifier,
    state: EditProfileState,
    onAvatarClick: () -> Unit = {},
    onNameChange: (String) -> Unit = {},
    onUrlChange: (String) -> Unit = {},
) {
    Column(
        modifier = modifier
            .padding(horizontal = Spacing.large)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.large)
    ) {
        AsyncImage(
            model = state.photoUri,
            contentDescription = "photo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = Spacing.large)
                .clip(CircleShape)
                .size(128.dp)
                .clickable { onAvatarClick() },
            error = painterResource(R.drawable.profile)
        )

        TextField(
            value = state.name,
            onValueChange = { onNameChange(it) },
            label = { Text(stringResource(R.string.name_label)) },
        )

        TextField(
            value = state.url,
            onValueChange = { onUrlChange(it) },
            label = { Text(stringResource(R.string.link_label)) },
        )
    }
}