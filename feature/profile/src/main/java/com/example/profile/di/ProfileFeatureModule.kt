package com.example.profile.di

import androidx.datastore.core.DataStore
import com.example.core.navigation.EntryProviderInstaller
import com.example.core.navigation.Route
import com.example.profile.data.dataSource.DataSourceProvider
import com.example.profile.data.repository.IProfileRepository
import com.example.profile.data.repository.ProfileRepository
import com.example.profile.domain.interactor.ProfileInteractor
import com.example.profile.domain.model.ProfileEntity
import com.example.profile.presentation.screen.EditProfileScreen
import com.example.profile.presentation.viewModel.EditProfileViewModel
import com.example.profile.presentation.viewModel.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val PROFILE_QUALIFIER = "profileQualifier"

data object EditProfile : Route

val profileFeatureModule = module {

    factory<DataStore<ProfileEntity>>(named("profile")) {
        DataSourceProvider(get()).provide()
    }

    single<IProfileRepository> { ProfileRepository() }

    single { ProfileInteractor(get()) }

    viewModel { ProfileViewModel(get(), get()) }
    viewModel { EditProfileViewModel(get(), get(), get()) }

    factory<EntryProviderInstaller>(qualifier = named(PROFILE_QUALIFIER)) {
        {
            entry<EditProfile> {
                EditProfileScreen()
            }
        }
    }
}