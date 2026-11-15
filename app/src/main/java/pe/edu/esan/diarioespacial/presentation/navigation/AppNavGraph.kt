package pe.edu.esan.diarioespacial.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import pe.edu.esan.diarioespacial.presentation.asteroids.AsteroidDetailScreen
import pe.edu.esan.diarioespacial.presentation.asteroids.AsteroidListScreen
import pe.edu.esan.diarioespacial.presentation.asteroids.AsteroidViewModel
import pe.edu.esan.diarioespacial.presentation.asteroids.FavoriteAsteroidsScreen
import pe.edu.esan.diarioespacial.presentation.images.ApodDetailScreen
import pe.edu.esan.diarioespacial.presentation.images.ApodListScreen
import pe.edu.esan.diarioespacial.presentation.images.ApodViewModel
import pe.edu.esan.diarioespacial.presentation.images.FavoriteApodScreen

// BASE COMÚN: navegación principal con barra inferior (Imágenes y Asteroides).
// Cada opción tiene su propio grafo interno: "imagenes/..." (Alumno 1) y "asteroides/..." (Alumno 2).
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: ""
    //Alumno 1: un solo ViewModel para la lista, el detalle y "Mis imágenes favoritas"
    val apodViewModel: ApodViewModel = viewModel()
    //Alumno 2: un solo ViewModel para la lista, el detalle y "Mis asteroides"
    val asteroidViewModel: AsteroidViewModel = viewModel()

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute.startsWith("imagenes"),
                    onClick = { navController.navigateToTab("imagenes") },
                    icon = { Icon(Icons.Filled.Image, contentDescription = null) },
                    label = { Text("Imágenes") }
                )
                NavigationBarItem(
                    selected = currentRoute.startsWith("asteroides"),
                    onClick = { navController.navigateToTab("asteroides") },
                    icon = { Icon(Icons.Filled.Public, contentDescription = null) },
                    label = { Text("Asteroides") }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "imagenes",
            modifier = Modifier.padding(paddingValues)
        ) {
            //Imágenes - APOD (Alumno 1)
            navigation(startDestination = "imagenes/lista", route = "imagenes") {
                composable("imagenes/lista") {
                    ApodListScreen(
                        viewModel = apodViewModel,
                        onOpenDetail = { date -> navController.navigate("imagenes/detalle/$date") },
                        onOpenFavorites = { navController.navigate("imagenes/favoritas") }
                    )
                }
                composable("imagenes/detalle/{date}") { entry ->
                    ApodDetailScreen(
                        viewModel = apodViewModel,
                        date = entry.arguments?.getString("date") ?: "",
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("imagenes/favoritas") {
                    FavoriteApodScreen(
                        viewModel = apodViewModel,
                        onOpenDetail = { date -> navController.navigate("imagenes/detalle/$date") },
                        onBack = { navController.popBackStack() }
                    )
                }
            }

            //Asteroides - NeoWs (Alumno 2)
            navigation(startDestination = "asteroides/lista", route = "asteroides") {
                composable("asteroides/lista") {
                    AsteroidListScreen(
                        viewModel = asteroidViewModel,
                        onOpenDetail = { id -> navController.navigate("asteroides/detalle/$id") },
                        onOpenFavorites = { navController.navigate("asteroides/favoritos") }
                    )
                }
                composable("asteroides/detalle/{id}") { entry ->
                    AsteroidDetailScreen(
                        viewModel = asteroidViewModel,
                        asteroidId = entry.arguments?.getString("id") ?: "",
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("asteroides/favoritos") {
                    FavoriteAsteroidsScreen(
                        viewModel = asteroidViewModel,
                        onOpenDetail = { id -> navController.navigate("asteroides/detalle/$id") },
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

// Cambia de pestaña sin apilar pantallas y recordando dónde se quedó cada opción
private fun androidx.navigation.NavController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
