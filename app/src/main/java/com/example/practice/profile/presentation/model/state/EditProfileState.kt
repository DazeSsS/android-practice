package com.example.practice.profile.presentation.model.state

import android.net.Uri

interface EditProfileState {
    val photoUri: Uri
    val name: String
    val url: String
    val isNeedToShowPermission: Boolean
    val isNeedToShowSelect: Boolean
}