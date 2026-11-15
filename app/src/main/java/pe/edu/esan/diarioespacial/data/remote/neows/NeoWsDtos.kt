package pe.edu.esan.diarioespacial.data.remote.neows

import com.google.gson.annotations.SerializedName

// DTO = lo que llega de la API tal cual. Todo es nullable para que la app no se cierre
// si la NASA manda un campo nulo o no lo manda.

// Los asteroides vienen agrupados por fecha: { "2026-11-14": [ ... ], "2026-11-13": [ ... ] }
data class NeoFeedResponse(
    @SerializedName("near_earth_objects") val nearEarthObjects: Map<String, List<NeoDto>>?
)

data class NeoDto(
    val id: String?,
    val name: String?,
    @SerializedName("is_potentially_hazardous_asteroid") val isHazardous: Boolean?,
    @SerializedName("estimated_diameter") val estimatedDiameter: EstimatedDiameterDto?,
    @SerializedName("close_approach_data") val closeApproachData: List<CloseApproachDto>?
)

data class EstimatedDiameterDto(
    val meters: DiameterRangeDto?
)

data class DiameterRangeDto(
    @SerializedName("estimated_diameter_min") val min: Double?,
    @SerializedName("estimated_diameter_max") val max: Double?
)

data class CloseApproachDto(
    @SerializedName("close_approach_date") val date: String?,
    @SerializedName("epoch_date_close_approach") val epoch: Long?,
    @SerializedName("relative_velocity") val relativeVelocity: RelativeVelocityDto?,
    @SerializedName("miss_distance") val missDistance: MissDistanceDto?
)

// Ojo: la velocidad y la distancia llegan como TEXTO ("40126.2500767766")
data class RelativeVelocityDto(
    @SerializedName("kilometers_per_hour") val kilometersPerHour: String?
)

data class MissDistanceDto(
    val kilometers: String?
)
