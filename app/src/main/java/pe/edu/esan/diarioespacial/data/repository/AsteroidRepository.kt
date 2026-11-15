package pe.edu.esan.diarioespacial.data.repository

import kotlinx.coroutines.flow.Flow
import pe.edu.esan.diarioespacial.data.local.AsteroidDao
import pe.edu.esan.diarioespacial.data.local.AsteroidEntity
import pe.edu.esan.diarioespacial.data.remote.RetrofitInstance
import pe.edu.esan.diarioespacial.data.remote.neows.NeoDto
import java.time.LocalDate

// Une la API (NeoWs) y la base local (Room). La pantalla solo lee de Room.
class AsteroidRepository(private val dao: AsteroidDao) {

    // Últimos 7 días: desde hace 6 días hasta hoy (formato AAAA-MM-DD)
    val startDate: String = LocalDate.now().minusDays(6).toString()
    private val endDate: String = LocalDate.now().toString()

    fun getAsteroids(): Flow<List<AsteroidEntity>> = dao.getAsteroids(startDate)

    fun getFavorites(): Flow<List<AsteroidEntity>> = dao.getFavorites()

    // Consulta la API y guarda en Room. Al guardarse, Room avisa a la pantalla sola.
    suspend fun refreshAsteroids(): Result<Unit> {
        return try {
            val response = RetrofitInstance.neoWsApi.getFeed(startDate, endDate)
            // Las listas vienen agrupadas por fecha: se juntan en una sola
            val dtos = response.nearEarthObjects?.values?.flatten().orEmpty()
            // Se conserva la marca de favorito de lo que ya estaba guardado
            val favoriteIds = dao.getFavoriteIds().toSet()
            val entities = dtos.mapNotNull { it.toEntity() }
                .map { it.copy(isFavorite = it.id in favoriteIds) }

            dao.insertAll(entities)
            dao.deleteOldNotFavorites(startDate)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleFavorite(asteroid: AsteroidEntity) {
        dao.updateFavorite(asteroid.id, !asteroid.isFavorite)
    }
}

// Conversión explícita DTO -> Entity. Si falta el id no se puede guardar: se descarta (null).
// Los números que llegan como texto se convierten con toDoubleOrNull() para no romper la app.
fun NeoDto.toEntity(): AsteroidEntity? {
    val asteroidId = id ?: return null
    val approach = closeApproachData?.firstOrNull()
    return AsteroidEntity(
        id = asteroidId,
        name = name ?: "Sin nombre",
        approachDate = approach?.date ?: "",
        approachEpoch = approach?.epoch ?: 0L,
        diameterMinMeters = estimatedDiameter?.meters?.min,
        diameterMaxMeters = estimatedDiameter?.meters?.max,
        velocityKmh = approach?.relativeVelocity?.kilometersPerHour?.toDoubleOrNull(),
        missDistanceKm = approach?.missDistance?.kilometers?.toDoubleOrNull(),
        isHazardous = isHazardous ?: false
    )
}
