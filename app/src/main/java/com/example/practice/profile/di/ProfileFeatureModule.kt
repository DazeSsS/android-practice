package com.example.practice.profile.di

import androidx.datastore.core.DataStore
import com.example.practice.profile.data.dataSource.DataSourceProvider
import com.example.practice.profile.data.repository.IProfileRepository
import com.example.practice.profile.data.repository.ProfileRepository
import com.example.practice.profile.domain.interactor.ProfileInteractor
import com.example.practice.profile.domain.model.ProfileEntity
import com.example.practice.profile.presentation.viewModel.EditProfileViewModel
import com.example.practice.profile.presentation.viewModel.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val profileFeatureModule = module {

    factory<DataStore<ProfileEntity>>(named("profile")) {
        DataSourceProvider(get()).provide()
    }

    single<IProfileRepository> { ProfileRepository() }

    single { ProfileInteractor(get()) }

    viewModel { ProfileViewModel(get(), get()) }
    viewModel { EditProfileViewModel(get(), get(), get()) }
}