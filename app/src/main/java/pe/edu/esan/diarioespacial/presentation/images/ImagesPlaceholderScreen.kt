package pe.edu.esan.diarioespacial.presentation.images

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// BASE COMÚN: pantalla temporal. El Alumno 1 la reemplaza por su lista de APOD.
@Composable
fun ImagesPlaceholderScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Imágenes", style = MaterialTheme.typography.titleLarge)
        Text("Próximamente: imagen astronómica del día (APOD).")
    }
}