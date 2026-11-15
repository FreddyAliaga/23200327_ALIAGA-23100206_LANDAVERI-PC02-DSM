package pe.edu.esan.diarioespacial.presentation.images

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import pe.edu.esan.diarioespacial.data.local.ApodEntity

// Tarjeta de una imagen: se usa en la lista y en "Mis imágenes favoritas"
@Composable
fun ApodCard(
    image: ApodEntity,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column {
            ApodImage(image, height = 200.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(image.title, style = MaterialTheme.typography.titleMedium)
                    Text(formatApodDate(image.date), style = MaterialTheme.typography.bodyMedium)
                }
                ApodFavoriteButton(image.isFavorite, onToggleFavorite)
            }
        }
    }
}

// Si es video NO se intenta cargar la imagen: se muestra "Contenido en video"
@Composable
fun ApodImage(image: ApodEntity, height: Dp) {
    val isVideo = image.mediaType == "video"
    if (isVideo || image.hdUrl == null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(if (isVideo) "Contenido en video" else "Imagen no disponible")
        }
    } else {
        AsyncImage(
            model = image.hdUrl,
            contentDescription = image.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
        )
    }
}

// Corazón lleno = favorito; corazón vacío = no favorito
@Composable
fun ApodFavoriteButton(isFavorite: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = if (isFavorite) "Quitar de favoritos" else "Marcar como favorita",
            tint = if (isFavorite) Color(0xFFE53935) else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// Centra un mensaje o el indicador de carga en el espacio libre
@Composable
fun ApodCenteredBox(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }
}

// "2026-11-14" -> "14/11/2026"
fun formatApodDate(date: String): String {
    val parts = date.split("-")
    return if (parts.size == 3) "${parts[2]}/${parts[1]}/${parts[0]}" else date
}
