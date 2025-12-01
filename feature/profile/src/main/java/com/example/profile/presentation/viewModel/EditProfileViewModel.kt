package com.example.profile.presentation.viewModel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.navigation.Route
import com.example.core.navigation.TopLevelBackStack
import com.example.profile.domain.interactor.ProfileInteractor
import com.example.profile.presentation.model.state.EditProfileState
import com.example.core.notifications.NotificationsReceiver
import com.example.core.notifications.NotificationsReceiver.Companion.NOTIFICATION_KEY
import com.example.core.core.tryParse
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class EditProfileViewModel(
    private val topLevelBackStack: TopLevelBackStack<Route>,
    private val interactor: ProfileInteractor,
    private val context: Context,
) : ViewModel() {
    private val mutableState = MutableEditProfileState()
    val state = mutableState as EditProfileState

    private val formatter = DateTimeFormatter.ofPattern("HH:mm")

    init {
        viewModelScope.launch {
            interactor.getProfile()?.let {
                mutableState.name = it.name
                mutableState.url = it.url
                mutableState.photoUri = Uri.parse(it.photoUri)
                tryParse(it.time)?.let { time ->
                    mutableState.time = time
                    updateTimeString()
                }
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

    fun onTimeChange(time: String) {
        mutableState.timeString = time
        validateTime()
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

    fun onTimeInputClick() {
        mutableState.isNeedToShowTimePicker = true
    }

    fun onTimeConfirm(h: Int, m: Int) {
        mutableState.time = LocalTime.of(h, m)
        mutableState.timeError = null
        updateTimeString()
        onTimeDialogDismiss()
    }

    fun onTimeDialogDismiss() {
        mutableState.isNeedToShowTimePicker = false
    }

    fun onDoneClick() {
        validateTime()
        if (mutableState.timeError != null) return
        viewModelScope.launch {
            interactor.setProfile(
                mutableState.photoUri.toString(),
                mutableState.name,
                mutableState.url,
                mutableState.time
            )
            saveNotification()
            onBack()
        }
    }

    fun onBack() {
        topLevelBackStack.removeLast()
    }

    private fun saveNotification() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        var dateTime = LocalDateTime.of(LocalDate.now(), state.time)

        if (dateTime.isBefore(LocalDateTime.now())) {
            dateTime = dateTime.plusDays(1)
        }

        val timeInMillis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val notifyIntent = Intent(context, NotificationsReceiver::class.java)

        notifyIntent.putExtras(
            Bundle().apply {
                putString(NOTIFICATION_KEY, "Начинается ваша любимая пара, ${state.name}!")
            }
        )

        val notifyPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            notifyIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                notifyPendingIntent
            )
        } catch (e: SecurityException) {
            Log.d("alarmManager", "Failed to set reminder")
        }
    }

    private fun validateTime() {
        try {
            mutableState.time = LocalTime.parse(mutableState.timeString, formatter)
            mutableState.timeError = null
        } catch (e: Exception) {
            mutableState.timeError = "Некорректный формат времени"
        }
    }

    private fun updateTimeString() {
        mutableState.timeString = formatter.format(state.time)
    }

    private class MutableEditProfileState : EditProfileState {
        override var photoUri: Uri by mutableStateOf(Uri.EMPTY)
        override var name: String by mutableStateOf("")
        override var url: String by mutableStateOf("")
        override var time: LocalTime by mutableStateOf(LocalTime.now())
        override var timeString: String by mutableStateOf("")
        override var timeError: String? by mutableStateOf(null)
        override var isNeedToShowPermission: Boolean by mutableStateOf(false)
        override var isNeedToShowSelect: Boolean by mutableStateOf(false)
        override var isNeedToShowTimePicker: Boolean by mutableStateOf(false)
    }
}