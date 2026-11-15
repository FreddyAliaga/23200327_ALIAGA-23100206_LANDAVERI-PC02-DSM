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
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import pe.edu.esan.diarioespacial.presentation.images.ImagesPlaceholderScreen

// BASE COMÚN: navegación principal con barra inferior (Imágenes y Asteroides).
// Cada opción tiene su propio grafo interno: "imagenes/..." (Alumno 1) y "asteroides/..." (Alumno 2).
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: ""

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
                composable("imagenes/lista") { ImagesPlaceholderScreen() }
            }

            //Asteroides - NeoWs (Alumno 2)
            navigation(startDestination = "asteroides/lista", route = "asteroides") {
                composable("asteroides/lista") { Text("Próximamente: asteroides cercanos (NeoWs).") }
            }
        }
    }
}

// Cambia de pestaña sin apilar pantallas y recordando dónde se quedó cada opción
private fun NavController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
