package pe.edu.esan.diarioespacial.presentation.images

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.esan.diarioespacial.data.local.ApodEntity
import pe.edu.esan.diarioespacial.data.local.AppDatabase
import pe.edu.esan.diarioespacial.data.repository.ApodRepository

// Estado de la opción Imágenes (lista, detalle y "Mis imágenes favoritas")
data class ApodUiState(
    val images: List<ApodEntity> = emptyList(),
    val favorites: List<ApodEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    // El detalle se busca en lo guardado (un favorito viejo puede no estar en los últimos 7 días)
    fun findByDate(date: String): ApodEntity? =
        images.firstOrNull { it.date == date } ?: favorites.firstOrNull { it.date == date }
}

class ApodViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ApodRepository(
        AppDatabase.getDatabase(application).apodDao()
    )

    private val _uiState = MutableStateFlow(ApodUiState())
    val uiState: StateFlow<ApodUiState> = _uiState.asStateFlow()

    init {
        // La pantalla se alimenta de Room: cada cambio en la tabla llega aquí solo
        viewModelScope.launch {
            repository.getImages().collect { list ->
                _uiState.update { it.copy(images = list) }
            }
        }
        viewModelScope.launch {
            repository.getFavorites().collect { list ->
                _uiState.update { it.copy(favorites = list) }
            }
        }
    }

    // Consulta la API y guarda en Room (si falla, se sigue mostrando lo guardado)
    fun loadImages() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = repository.refreshImages()
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

    fun toggleFavorite(image: ApodEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(image)
        }
    }
}
