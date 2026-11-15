package pe.edu.esan.diarioespacial.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {

    // Asteroides desde una fecha, los más recientes primero
    @Query("SELECT * FROM asteroids WHERE approachDate >= :startDate ORDER BY approachEpoch DESC")
    fun getAsteroids(startDate: String): Flow<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroids WHERE isFavorite = 1 ORDER BY approachEpoch DESC")
    fun getFavorites(): Flow<List<AsteroidEntity>>

    @Query("SELECT id FROM asteroids WHERE isFavorite = 1")
    suspend fun getFavoriteIds(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(asteroids: List<AsteroidEntity>)

    @Query("UPDATE asteroids SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: String, isFavorite: Boolean)

    // Limpia los asteroides viejos que no son favoritos
    @Query("DELETE FROM asteroids WHERE isFavorite = 0 AND approachDate < :startDate")
    suspend fun deleteOldNotFavorites(startDate: String)
}
