package pe.edu.esan.diarioespacial.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ApodDao {

    // Imágenes desde una fecha, de la más reciente a la más antigua
    @Query("SELECT * FROM apod_images WHERE date >= :startDate ORDER BY date DESC")
    fun getImages(startDate: String): Flow<List<ApodEntity>>

    @Query("SELECT * FROM apod_images WHERE isFavorite = 1 ORDER BY date DESC")
    fun getFavorites(): Flow<List<ApodEntity>>

    @Query("SELECT date FROM apod_images WHERE isFavorite = 1")
    suspend fun getFavoriteDates(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(images: List<ApodEntity>)

    @Query("UPDATE apod_images SET isFavorite = :isFavorite WHERE date = :date")
    suspend fun updateFavorite(date: String, isFavorite: Boolean)

    // Limpia las imágenes viejas que no son favoritas
    @Query("DELETE FROM apod_images WHERE isFavorite = 0 AND date < :startDate")
    suspend fun deleteOldNotFavorites(startDate: String)
}
