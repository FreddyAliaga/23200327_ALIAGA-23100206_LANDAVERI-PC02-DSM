package pe.edu.esan.diarioespacial.presentation.asteroids

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pe.edu.esan.diarioespacial.data.local.AsteroidEntity
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

// Tarjeta de un asteroide: se usa en la lista y en "Mis asteroides"
@Composable
fun AsteroidCard(
    asteroid: AsteroidEntity,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(asteroid.name, style = MaterialTheme.typography.titleMedium)
                if (asteroid.isHazardous) HazardLabel()
                Text("Aproximación: ${formatDate(asteroid.approachDate)}")
                Text("Diámetro máx.: ${formatNumber(asteroid.diameterMaxMeters, "m")}")
                Text("Velocidad: ${formatNumber(asteroid.velocityKmh, "km/h")}")
                Text("Distancia: ${formatNumber(asteroid.missDistanceKm, "km")}")
            }
            FavoriteButton(asteroid.isFavorite, onToggleFavorite)
        }
    }
}

// Estrella llena = favorito; estrella vacía = no favorito
@Composable
fun FavoriteButton(isFavorite: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
            contentDescription = if (isFavorite) "Quitar de favoritos" else "Marcar como favorito",
            tint = if (isFavorite) Color(0xFFFFD700) else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// Etiqueta roja para los potencialmente peligrosos
@Composable
fun HazardLabel() {
    Text(
        text = "⚠ Potencialmente peligroso",
        color = MaterialTheme.colorScheme.onError,
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.error, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    )
}

// Máximo dos decimales y con su unidad: 40126.2500767766 -> "40,126.25 km/h"
private val numberFormat = DecimalFormat("#,##0.##", DecimalFormatSymbols(Locale.US))

fun formatNumber(value: Double?, unit: String): String =
    if (value == null) "Sin dato" else "${numberFormat.format(value)} $unit"

// "2026-11-14" -> "14/11/2026"
fun formatDate(date: String): String {
    val parts = date.split("-")
    return if (parts.size == 3) "${parts[2]}/${parts[1]}/${parts[0]}" else "Sin fecha"
}
