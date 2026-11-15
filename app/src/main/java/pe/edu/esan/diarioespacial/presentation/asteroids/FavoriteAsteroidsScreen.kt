package pe.edu.esan.diarioespacial.presentation.asteroids

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

// "Mis asteroides": solo los marcados como favoritos (leídos desde Room)
@Composable
fun FavoriteAsteroidsScreen(
    viewModel: AsteroidViewModel,
    onOpenDetail: (String) -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text("Mis asteroides", style = MaterialTheme.typography.titleLarge)
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (state.favorites.isEmpty()) {
            CenteredBox { Text("Aún no tienes asteroides favoritos. Márcalos con la estrella.") }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.favorites, key = { it.id }) { asteroid ->
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
