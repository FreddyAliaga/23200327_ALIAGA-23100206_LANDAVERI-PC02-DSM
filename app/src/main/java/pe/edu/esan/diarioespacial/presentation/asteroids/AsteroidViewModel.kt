package pe.edu.esan.diarioespacial.presentation.asteroids

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.esan.diarioespacial.data.local.AppDatabase
import pe.edu.esan.diarioespacial.data.local.AsteroidEntity
import pe.edu.esan.diarioespacial.data.repository.AsteroidRepository

// Estado de la opción Asteroides (lista, detalle y "Mis asteroides")
data class AsteroidUiState(
    val asteroids: List<AsteroidEntity> = emptyList(),
    val favorites: List<AsteroidEntity> = emptyList(),
    val onlyHazardous: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    // Lo que se ve en la lista según el interruptor "Solo potencialmente peligrosos"
    val visibleAsteroids: List<AsteroidEntity>
        get() = if (onlyHazardous) asteroids.filter { it.isHazardous } else asteroids

    // El detalle se busca en lo guardado (un favorito viejo puede no estar en los últimos 7 días)
    fun findById(id: String): AsteroidEntity? =
        asteroids.firstOrNull { it.id == id } ?: favorites.firstOrNull { it.id == id }
}

class AsteroidViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AsteroidRepository(
        AppDatabase.getDatabase(application).asteroidDao()
    )

    private val _uiState = MutableStateFlow(AsteroidUiState())
    val uiState: StateFlow<AsteroidUiState> = _uiState.asStateFlow()

    init {
        // La pantalla se alimenta de Room: cada cambio en la tabla llega aquí solo
        viewModelScope.launch {
            repository.getAsteroids().collect { list ->
                _uiState.update { it.copy(asteroids = list) }
            }
        }
        viewModelScope.launch {
            repository.getFavorites().collect { list ->
                _uiState.update { it.copy(favorites = list) }
            }
        }
    }

    // Consulta la API y guarda en Room (si falla, se sigue mostrando lo guardado)
    fun loadAsteroids() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = repository.refreshAsteroids()
            result.fold(
                onSuccess = { _uiState.update { it.copy(isLoading = false) } },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "No se pudo consultar la NASA (${e.message}).")
                    }
                }
            )
        }
    }

    fun onOnlyHazardousChange(value: Boolean) {
        _uiState.update { it.copy(onlyHazardous = value) }
    }

    fun toggleFavorite(asteroid: AsteroidEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(asteroid)
        }
    }
}
