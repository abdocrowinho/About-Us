package org.aboutus.project.features.earth.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

@Serializable
data class GeoJsonFeatureCollection(
    val features: List<GeoJsonFeature>
)

@Serializable
data class GeoJsonFeature(
    val properties: GeoJsonProperties,
    val geometry: GeoJsonGeometry
)

@Serializable
data class GeoJsonProperties(
    @SerialName("ADMIN") val name: String? = null,
    @SerialName("ISO_A2") val isoCode: String? = null,
    @SerialName("ISO_A3") val isoCodeFallback: String? = null
)

@Serializable
data class GeoJsonGeometry(
    val type: String,
    val coordinates: JsonArray
)
