package pe.edu.esan.diarioespacial.data.remote.apod

import retrofit2.http.GET
import retrofit2.http.Query

interface ApodService {

    // Imágenes entre dos fechas (AAAA-MM-DD). La api_key la agrega RetrofitInstance.
    @GET("wp-json/wp/v2/apod-basic")
    suspend fun getImages(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): List<ApodDto>
}
