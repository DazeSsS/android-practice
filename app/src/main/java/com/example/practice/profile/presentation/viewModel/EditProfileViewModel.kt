package com.example.practice.profile.presentation.viewModel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice.navigation.Route
import com.example.practice.navigation.TopLevelBackStack
import com.example.practice.profile.domain.interactor.ProfileInteractor
import com.example.practice.profile.presentation.model.state.EditProfileState
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val topLevelBackStack: TopLevelBackStack<Route>,
    private val interactor: ProfileInteractor,
) : ViewModel() {
    private val mutableState = MutableEditProfileState()
    val state = mutableState as EditProfileState

    init {
        viewModelScope.launch {
            interactor.getProfile()?.let {
                mutableState.name = it.name
                mutableState.url = it.url
                mutableState.photoUri = Uri.parse(it.photoUri)
            }
        }
        mutableState.isNeedToShowPermission = true
    }

    fun onNameChange(name: String) {
        mutableState.name = name
    }

    fun onUrlChange(url: String) {
        mutableState.url = url
    }

    fun onImageSelect(uri: Uri?) {
        uri?.let { mutableState.photoUri = it }
    }

    fun onAvatarClick() {
        mutableState.isNeedToShowSelect = true
    }

    fun onPermissionClose() {
        mutableState.isNeedToShowPermission = false
    }

    fun onSelectDismiss() {
        mutableState.isNeedToShowSelect = false
    }

    fun onDoneClick() {
        viewModelScope.launch {
            interactor.setProfile(
                mutableState.photoUri.toString(),
                mutableState.name,
                mutableState.url
            )
            onBack()
        }
    }

    fun onBack() {
        topLevelBackStack.removeLast()
    }

    private class MutableEditProfileState : EditProfileState {
        override var photoUri: Uri by mutableStateOf(Uri.EMPTY)
        override var name: String by mutableStateOf("")
        override var url: String by mutableStateOf("")
        override var isNeedToShowPermission: Boolean by mutableStateOf(false)
        override var isNeedToShowSelect: Boolean by mutableStateOf(false)
    }
}