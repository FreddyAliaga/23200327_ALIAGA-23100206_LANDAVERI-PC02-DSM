package pe.edu.esan.diarioespacial.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// BASE COMÚN: una sola base de datos para las dos opciones (ver README).
// Versión 2: se agregó la tabla de imágenes (Alumno 1).
@Database(entities = [AsteroidEntity::class, ApodEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun asteroidDao(): AsteroidDao

    abstract fun apodDao(): ApodDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "diario_espacial_db"
                )
                    // Es una caché de la API: si cambia la versión se recrea sin problema
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
