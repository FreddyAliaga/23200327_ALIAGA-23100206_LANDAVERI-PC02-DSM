package pe.edu.esan.diarioespacial.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabla de asteroides (Alumno 2). Las pantallas siempre leen de aquí, no de la API.
@Entity(tableName = "asteroids")
data class AsteroidEntity(
    @PrimaryKey val id: String,
    val name: String,
    val approachDate: String,        // AAAA-MM-DD
    val approachEpoch: Long,         // para ordenar del más reciente al más antiguo
    val diameterMinMeters: Double?,
    val diameterMaxMeters: Double?,
    val velocityKmh: Double?,
    val missDistanceKm: Double?,
    val isHazardous: Boolean,
    val isFavorite: Boolean = false
)
