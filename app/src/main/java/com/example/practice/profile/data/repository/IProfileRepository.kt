package com.example.practice.profile.data.repository

import com.example.practice.profile.domain.model.ProfileEntity
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalTime

interface IProfileRepository {

    suspend fun observeProfile(): Flow<ProfileEntity>

    suspend fun getProfile(): ProfileEntity?

    suspend fun setProfile(
        photoUri: String,
        name: String,
        url: String,
        time: LocalTime = LocalTime.now(),
    ): ProfileEntity
}