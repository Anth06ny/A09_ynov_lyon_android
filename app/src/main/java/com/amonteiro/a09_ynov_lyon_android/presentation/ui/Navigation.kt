package com.amonteiro.a09_ynov_lyon_android.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.amonteiro.a09_ynov_lyon_android.domain.model.Weather
import com.amonteiro.a09_ynov_lyon_android.presentation.MainViewModel
import com.amonteiro.a09_ynov_lyon_android.presentation.ui.screens.DetailScreen
import com.amonteiro.a09_ynov_lyon_android.presentation.ui.screens.SearchScreen
import kotlinx.serialization.Serializable

//Les routes implémentent NavKey
class Routes {
    @kotlinx.serialization.Serializable
    data object SearchRoute : NavKey

    //Les paramètres peuvent être des objets, à condition qu'ils soient @Serializable
    @Serializable
    data class DetailRoute(val weather: Weather) : NavKey
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    //La pile d'écrans, avec l'écran de départ. Sauvegardée lors des rotations
    val backStack = rememberNavBackStack(Routes.SearchRoute)
    val mainViewModel : MainViewModel = viewModel()

    NavDisplay(
        backStack = backStack,
        //Retour système (bouton ou geste) : on retire le dernier écran
        onBack = { backStack.removeLastOrNull() },
        modifier = modifier,
        entryProvider = entryProvider {
            //Route 1 vers notre SearchScreen
            entry<Routes.SearchRoute> {
                SearchScreen(
                    mainViewModel = mainViewModel,
                    //On remonte l'événement : la navigation reste dans Navigation.kt
                    onPictureClick = { backStack.add(Routes.DetailRoute(it)) }
                )
            }

            //Route 2 vers un écran de détail
            //La route est reçue directement, avec ses paramètres
            entry<Routes.DetailRoute> { detailRoute ->
                DetailScreen(data = detailRoute.weather,
                    onBackClick = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}