package pe.edu.esan.diarioespacial.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabla de imágenes APOD (Alumno 1). Hay una imagen por día: la fecha es la clave.
@Entity(tableName = "apod_images")
data class ApodEntity(
    @PrimaryKey val date: String,    // AAAA-MM-DD
    val title: String,
    val explanation: String,
    val mediaType: String,           // "image" o "video"
    val hdUrl: String?,
    val credit: String?,
    val isFavorite: Boolean = false
)
