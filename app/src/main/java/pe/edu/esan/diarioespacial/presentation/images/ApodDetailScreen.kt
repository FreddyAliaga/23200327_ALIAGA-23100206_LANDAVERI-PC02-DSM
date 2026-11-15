package pe.edu.esan.diarioespacial.presentation.images

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Detalle de una imagen: imagen en grande, título, fecha, explicación y crédito (si existe)
@Composable
fun ApodDetailScreen(
    viewModel: ApodViewModel,
    date: String,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val image = state.findByDate(date)

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
                image?.title ?: "Imagen",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            image?.let { ApodFavoriteButton(it.isFavorite) { viewModel.toggleFavorite(it) } }
        }

        if (image == null) {
            Text("No se encontró la imagen.")
            return@Column
        }

        ApodImage(image, height = 320.dp)
        Text(formatApodDate(image.date), style = MaterialTheme.typography.labelLarge)
        Text(image.explanation, style = MaterialTheme.typography.bodyLarge)
        image.credit?.let {
            Text("Crédito: $it", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
