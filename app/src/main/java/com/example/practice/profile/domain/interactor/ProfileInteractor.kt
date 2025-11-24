package com.example.practice.profile.domain.interactor

import com.example.practice.profile.data.repository.IProfileRepository
import com.example.practice.profile.domain.model.ProfileEntity
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalTime

class ProfileInteractor(
    private val repository: IProfileRepository
) {
    suspend fun observeProfile(): Flow<ProfileEntity> = repository.observeProfile()

    suspend fun getProfile(): ProfileEntity? = repository.getProfile()

    suspend fun setProfile(photoUri: String, name: String, url: String, time: LocalTime) =
        repository.setProfile(
            photoUri = photoUri,
            name = name,
            url = url,
            time = time,
        )
}