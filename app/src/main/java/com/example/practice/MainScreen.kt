package com.example.practice

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.example.practice.movies.presentation.model.MovieUiModel
import com.example.practice.movies.presentation.screen.MovieDetailScreen
import com.example.practice.movies.presentation.screen.MovieListScreen
import com.example.practice.movies.presentation.viewModel.MovieDetailViewModel
import com.example.practice.movies.presentation.viewModel.MovieListViewModel
import com.example.practice.navigation.Route
import com.example.practice.navigation.TopLevelBackStack
import com.example.practice.uikit.Spacing
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
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
data class MovieDetail(val movie: MovieUiModel) : Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val topLevelBackStack by inject<TopLevelBackStack<Route>>(clazz = TopLevelBackStack::class.java)
    val sceneStrategy = remember { SinglePaneSceneStrategy<Route>() }

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
            sceneStrategy = sceneStrategy,
            entryDecorators = listOf(
                rememberSavedStateNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<Movies> {
                    val viewModel = koinViewModel<MovieListViewModel>()
                    MovieListScreen(viewModel)
                }
                entry<Favorites> {
                    FavoritesScreen()
                }
                entry<Profile> {
                    ProfileScreen()
                }
                entry<MovieDetail> {
                    val viewModel = koinViewModel<MovieDetailViewModel>() {
                        parametersOf(it.movie)
                    }
                    MovieDetailScreen(viewModel)
                }
            }
        )
    }
}

@Composable
fun FavoritesScreen() {
    Text(
        text = "This is favorites screen",
        style = MaterialTheme.typography.displayMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = Spacing.large)
    )
}

@Composable
fun ProfileScreen() {
    Text(
        text = "This is profile screen",
        style = MaterialTheme.typography.displayMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = Spacing.large)
    )
}