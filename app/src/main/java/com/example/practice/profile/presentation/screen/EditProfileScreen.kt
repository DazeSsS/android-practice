package com.example.practice.profile.presentation.screen

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import android.app.AlertDialog
import android.os.Build
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
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
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
import org.threeten.bp.LocalTime
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
            ActivityResultContracts.RequestMultiplePermissions()
        ) { map: Map<String, Boolean> ->
            if (map.values.contains(false)) {
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
            viewModel = viewModel,
        )
    }

    if (state.isNeedToShowPermission) {
        LaunchedEffect(Unit) {
            val permissions = mutableListOf<String>()
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }

            if (
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(Manifest.permission.POST_NOTIFICATIONS)
            }

            requestPermissionLauncher.launch(permissions.toTypedArray())
        }
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
    viewModel: EditProfileViewModel,
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
                .clickable { viewModel.onAvatarClick() },
            error = painterResource(R.drawable.profile)
        )

        TextField(
            value = state.name,
            onValueChange = { viewModel.onNameChange(it) },
            label = { Text(stringResource(R.string.name_label)) },
        )

        TextField(
            value = state.url,
            onValueChange = { viewModel.onUrlChange(it) },
            label = { Text(stringResource(R.string.link_label)) },
        )

        TextField(
            value = state.timeString,
            onValueChange = { viewModel.onTimeChange(it) },
            label = { Text(stringResource(R.string.time_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            isError = state.timeError != null,
            trailingIcon = {
                Icon(
                    painterResource(id = R.drawable.time),
                    null,
                    modifier = Modifier.clickable { viewModel.onTimeInputClick() })
            }
        )
        state.timeError?.let {
            Text(
                it,
                color = MaterialTheme.colorScheme.error,
            )
        }
        if (state.isNeedToShowTimePicker) {
            DialWithDialogExample(
                onConfirm = { h, m -> viewModel.onTimeConfirm(h, m) },
                onDismiss = { viewModel.onTimeDialogDismiss() },
                time = state.time
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialWithDialogExample(
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit,
    time: LocalTime
) {
    val timePickerState = rememberTimePickerState(
        initialHour = time.hour,
        initialMinute = time.minute,
        is24Hour = true,
    )

    TimePickerDialog(
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(timePickerState.hour, timePickerState.minute) }
    ) {
        TimePicker(
            state = timePickerState,
        )
    }
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Отмена")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        text = { content() }
    )
}