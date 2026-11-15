package pe.edu.esan.diarioespacial.presentation.asteroids

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Lista de asteroides de los últimos 7 días (leída desde Room)
@Composable
fun AsteroidListScreen(
    viewModel: AsteroidViewModel,
    onOpenDetail: (String) -> Unit,
    onOpenFavorites: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    // Al abrir la opción se consulta la API (lo que llega se guarda en Room)
    LaunchedEffect(Unit) {
        viewModel.loadAsteroids()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        //Título + botón "Mis asteroides"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Asteroides cercanos",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = onOpenFavorites) {
                Icon(Icons.Filled.Star, contentDescription = null)
                Text(" Mis asteroides")
            }
        }

        //Filtro
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Solo potencialmente peligrosos", modifier = Modifier.weight(1f))
            Switch(
                checked = state.onlyHazardous,
                onCheckedChange = { viewModel.onOnlyHazardousChange(it) }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Si falla la API se avisa, pero se sigue mostrando lo guardado en Room
        state.errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            TextButton(onClick = { viewModel.loadAsteroids() }) { Text("Reintentar") }
        }

        when {
            state.isLoading && state.asteroids.isEmpty() -> CenteredBox { CircularProgressIndicator() }
            state.visibleAsteroids.isEmpty() -> CenteredBox {
                Text(
                    if (state.onlyHazardous) "No hay asteroides potencialmente peligrosos en estos 7 días."
                    else "No hay asteroides para mostrar."
                )
            }
            else -> {
                if (state.isLoading) CircularProgressIndicator(modifier = Modifier.padding(bottom = 8.dp))
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.visibleAsteroids, key = { it.id }) { asteroid ->
                        AsteroidCard(
                            asteroid = asteroid,
                            onClick = { onOpenDetail(asteroid.id) },
                            onToggleFavorite = { viewModel.toggleFavorite(asteroid) }
                        )
                    }
                }
            }
        }
    }
}

// Centra un mensaje o el indicador de carga en el espacio libre
@Composable
fun CenteredBox(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }
}
