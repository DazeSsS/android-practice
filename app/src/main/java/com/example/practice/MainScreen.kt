package com.example.practice

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.example.core.navigation.EntryProviderInstaller
import com.example.core.navigation.Route
import com.example.core.navigation.TopLevelBackStack
import com.example.movies.di.MOVIE_QUALIFIER
import com.example.movies.presentation.screen.MovieListScreen
import com.example.profile.di.PROFILE_QUALIFIER
import com.example.profile.presentation.screen.ProfileScreen
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject

interface TopLevelRoute : Route {
    val icon: ImageVector
}

data object Movies : TopLevelRoute {
    override val icon = Icons.AutoMirrored.Filled.List
}

data object Favorites : TopLevelRoute {
    override val icon = Icons.Default.Favorite
}

data object Profile : TopLevelRoute {
    override val icon = Icons.Default.Person
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val topLevelBackStack by inject<TopLevelBackStack<Route>>(clazz = TopLevelBackStack::class.java)
    val dialogStrategy = remember { DialogSceneStrategy<Route>() }

    val moviesEntryProvider by inject<EntryProviderInstaller>(
        clazz = EntryProviderInstaller::class.java,
        qualifier = named(MOVIE_QUALIFIER)
    )
    val profileEntryProvider by inject<EntryProviderInstaller>(
        clazz = EntryProviderInstaller::class.java,
        qualifier = named(PROFILE_QUALIFIER)
    )

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        bottomBar = {
            NavigationBar() {
                listOf(Movies, Favorites, Profile).forEach { route ->
                    NavigationBarItem(
                        icon = { Icon(route.icon, null) },
                        selected = topLevelBackStack.topLevelKey == route,
                        onClick = {
                            topLevelBackStack.addTopLevel(route)
                        }
                    )
                }
            }
        }
    ) { padding ->
        NavDisplay(
            backStack = topLevelBackStack.backStack,
            onBack = { topLevelBackStack.removeLast() },
            modifier = Modifier.padding(padding),
            sceneStrategy = dialogStrategy,
            entryDecorators = listOf(
                rememberSavedStateNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<Movies> {
                    MovieListScreen()
                }
                entry<Favorites> {
                    MovieListScreen(favorites = true)
                }
                entry<Profile> {
                    ProfileScreen()
                }
                moviesEntryProvider.let { builder -> this.builder() }
                profileEntryProvider.let { builder -> this.builder() }
            }
        )
    }
}