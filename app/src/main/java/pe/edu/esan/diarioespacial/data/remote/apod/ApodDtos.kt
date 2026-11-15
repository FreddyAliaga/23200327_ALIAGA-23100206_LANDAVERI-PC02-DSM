package pe.edu.esan.diarioespacial.data.remote.apod

import com.google.gson.annotations.SerializedName

// DTO = lo que llega de APOD tal cual. Todo es nullable para que la app no se cierre
// si la NASA manda un campo nulo o no lo manda. El campo basic_html no se usa.
data class ApodDto(
    val date: String?,
    val title: String?,
    val explanation: String?,
    @SerializedName("media_type") val mediaType: String?,   // "image" o "video"
    val hdurl: String?,                                     // dirección de la imagen (url es la página del artículo)
    val credit: String?
)
