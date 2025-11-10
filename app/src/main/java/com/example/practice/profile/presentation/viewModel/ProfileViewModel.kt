package com.example.practice.profile.presentation.viewModel

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice.EditProfile
import com.example.practice.navigation.Route
import com.example.practice.navigation.TopLevelBackStack
import com.example.practice.profile.domain.interactor.ProfileInteractor
import com.example.practice.profile.presentation.model.state.ProfileState
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel(
    private val topLevelBackStack: TopLevelBackStack<Route>,
    private val interactor: ProfileInteractor,
) : ViewModel() {
    private val mutableState = MutableProfileState()
    val state = mutableState as ProfileState

    init {
        viewModelScope.launch {
            interactor.observeProfile().collect {
                mutableState.name = it.name
                mutableState.photoUri = Uri.parse(it.photoUri)
                mutableState.url = it.url
            }
        }
    }

    fun enqueueDownloadRequest(
        url: String,
        context: Context
    ) {
        val request: DownloadManager.Request = DownloadManager.Request(Uri.parse(url))
        with(request) {
            setTitle("Резюме")
            setMimeType("application/pdf")
            setDescription("Downloading pdf...")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setAllowedOverMetered(true)
            setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "Resume.pdf"
            )
        }
        val manager: DownloadManager =
            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

    fun navigateToDownloadInvoice(context: Context) {
        try {
            val file = File(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
                ),
                "Resume.pdf"
            )

            val uri = FileProvider.getUriForFile(
                context,
                context.applicationContext?.packageName + ".provider",
                file
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onEditClick() {
        topLevelBackStack.add(EditProfile)
    }

    private class MutableProfileState : ProfileState {
        override var name by mutableStateOf("")
        override var photoUri by mutableStateOf(Uri.EMPTY)
        override var url by mutableStateOf("")
    }
}