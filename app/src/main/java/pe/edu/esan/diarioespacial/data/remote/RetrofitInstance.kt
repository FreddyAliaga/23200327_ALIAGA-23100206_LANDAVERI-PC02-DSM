package pe.edu.esan.diarioespacial.data.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import pe.edu.esan.diarioespacial.BuildConfig
import pe.edu.esan.diarioespacial.data.remote.neows.NeoWsService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// APOD y NeoWs están en servidores distintos: por eso hay dos URL base
private const val NASA_BASE_URL = "https://api.nasa.gov/"          // NeoWs (Alumno 2)
private const val SCIENCE_BASE_URL = "https://science.nasa.gov/"   // APOD (Alumno 1)

// BASE COMÚN: la usan las dos opciones (ver README)
object RetrofitInstance {

    // Agrega api_key a TODAS las llamadas (la clave viene de local.properties vía BuildConfig)
    private val client = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
        val url = chain.request().url.newBuilder()
            .addQueryParameter("api_key", BuildConfig.NASA_API_KEY)
            .build()
        val request = chain.request().newBuilder().url(url).build()
        chain.proceed(request)
    }).build()

    private fun retrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //NeoWs - Asteroides (Alumno 2)
    val neoWsApi: NeoWsService by lazy {
        retrofit(NASA_BASE_URL).create(NeoWsService::class.java)
    }

    //APOD - Imágenes (Alumno 1): agregar aquí su servicio, por ejemplo:
    // val apodApi: ApodService by lazy { retrofit(SCIENCE_BASE_URL).create(ApodService::class.java) }
}
