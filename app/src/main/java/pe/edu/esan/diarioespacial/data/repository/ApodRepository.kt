package pe.edu.esan.diarioespacial.data.repository

import kotlinx.coroutines.flow.Flow
import pe.edu.esan.diarioespacial.data.local.ApodDao
import pe.edu.esan.diarioespacial.data.local.ApodEntity
import pe.edu.esan.diarioespacial.data.remote.RetrofitInstance
import pe.edu.esan.diarioespacial.data.remote.apod.ApodDto
import java.time.LocalDate

// Une la API (APOD) y la base local (Room). La pantalla solo lee de Room.
class ApodRepository(private val dao: ApodDao) {

    // Últimos 7 días: desde hace 6 días hasta hoy (formato AAAA-MM-DD)
    private val startDate: String = LocalDate.now().minusDays(6).toString()
    private val endDate: String = LocalDate.now().toString()

    fun getImages(): Flow<List<ApodEntity>> = dao.getImages(startDate)

    fun getFavorites(): Flow<List<ApodEntity>> = dao.getFavorites()

    // Consulta la API y guarda en Room. Al guardarse, Room avisa a la pantalla sola.
    suspend fun refreshImages(): Result<Unit> {
        return try {
            val dtos = RetrofitInstance.apodApi.getImages(startDate, endDate)
            // Se conserva la marca de favorito de lo que ya estaba guardado
            val favoriteDates = dao.getFavoriteDates().toSet()
            val entities = dtos.mapNotNull { it.toEntity() }
                .map { it.copy(isFavorite = it.date in favoriteDates) }

            dao.insertAll(entities)
            dao.deleteOldNotFavorites(startDate)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleFavorite(image: ApodEntity) {
        dao.updateFavorite(image.date, !image.isFavorite)
    }
}

// Conversión explícita DTO -> Entity. Si falta la fecha no se puede guardar: se descarta (null).
fun ApodDto.toEntity(): ApodEntity? {
    val imageDate = date ?: return null
    return ApodEntity(
        date = imageDate,
        title = title ?: "Sin título",
        explanation = explanation ?: "",
        mediaType = mediaType ?: "image",
        hdUrl = hdurl,
        credit = credit?.takeIf { it.isNotBlank() }
    )
}
