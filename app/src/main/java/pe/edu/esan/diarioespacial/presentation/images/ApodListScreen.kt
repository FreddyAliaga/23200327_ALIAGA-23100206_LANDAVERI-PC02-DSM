package pe.edu.esan.diarioespacial.presentation.images

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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Lista de imágenes APOD de los últimos 7 días (leída desde Room)
@Composable
fun ApodListScreen(
    viewModel: ApodViewModel,
    onOpenDetail: (String) -> Unit,
    onOpenFavorites: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    // Al abrir la opción se consulta la API (lo que llega se guarda en Room)
    LaunchedEffect(Unit) {
        viewModel.loadImages()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        //Título + botón "Mis imágenes favoritas"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Imágenes del espacio",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = onOpenFavorites) {
                Icon(Icons.Filled.Favorite, contentDescription = null)
                Text(" Mis favoritas")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Si falla la API se avisa, pero se sigue mostrando lo guardado en Room
        state.errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            TextButton(onClick = { viewModel.loadImages() }) { Text("Reintentar") }
        }

        when {
            state.isLoading && state.images.isEmpty() -> ApodCenteredBox { CircularProgressIndicator() }
            state.images.isEmpty() -> ApodCenteredBox { Text("No hay imágenes para mostrar.") }
            else -> {
                if (state.isLoading) CircularProgressIndicator(modifier = Modifier.padding(bottom = 8.dp))
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.images, key = { it.date }) { image ->
                        ApodCard(
                            image = image,
                            onClick = { onOpenDetail(image.date) },
                            onToggleFavorite = { viewModel.toggleFavorite(image) }
                        )
                    }
                }
            }
        }
    }
}
