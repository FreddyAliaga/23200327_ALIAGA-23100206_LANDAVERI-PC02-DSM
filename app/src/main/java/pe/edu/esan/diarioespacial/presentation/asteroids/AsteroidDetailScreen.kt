package pe.edu.esan.diarioespacial.presentation.asteroids

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Detalle de un asteroide (leído desde Room a través del ViewModel)
@Composable
fun AsteroidDetailScreen(
    viewModel: AsteroidViewModel,
    asteroidId: String,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val asteroid = state.findById(asteroidId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text(
                asteroid?.name ?: "Asteroide",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            asteroid?.let { FavoriteButton(it.isFavorite) { viewModel.toggleFavorite(it) } }
        }

        if (asteroid == null) {
            Text("No se encontró el asteroide.")
            return@Column
        }

        if (asteroid.isHazardous) HazardLabel()
        HorizontalDivider()
        DetailRow("Nombre", asteroid.name)
        DetailRow("Id", asteroid.id)
        DetailRow("Fecha de aproximación", formatDate(asteroid.approachDate))
        DetailRow("Diámetro mínimo estimado", formatNumber(asteroid.diameterMinMeters, "m"))
        DetailRow("Diámetro máximo estimado", formatNumber(asteroid.diameterMaxMeters, "m"))
        DetailRow("Velocidad", formatNumber(asteroid.velocityKmh, "km/h"))
        DetailRow("Distancia de paso", formatNumber(asteroid.missDistanceKm, "km"))
        DetailRow("¿Potencialmente peligroso?", if (asteroid.isHazardous) "Sí" else "No")
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
    }
}
