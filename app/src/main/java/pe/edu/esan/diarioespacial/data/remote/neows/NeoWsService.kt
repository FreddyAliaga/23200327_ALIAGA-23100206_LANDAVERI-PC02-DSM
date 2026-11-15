package pe.edu.esan.diarioespacial.data.remote.neows

import retrofit2.http.GET
import retrofit2.http.Query

interface NeoWsService {

    // Asteroides cercanos entre dos fechas (AAAA-MM-DD, máximo 7 días). La api_key la agrega RetrofitInstance.
    @GET("neo/rest/v1/feed")
    suspend fun getFeed(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): NeoFeedResponse
}
