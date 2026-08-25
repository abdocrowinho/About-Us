package org.aboutus.project.features.earth.data



import kotlinx.serialization.json.*
import org.aboutus.project.features.earth.domain.Country
import org.aboutus.project.features.earth.domain.LatLng

object GeoJsonParser {

    private val json = Json { ignoreUnknownKeys = true }

    fun parseCountries(jsonString: String): List<Country> {
        val collection = json.decodeFromString<GeoJsonFeatureCollection>(jsonString)
        val countries = mutableListOf<Country>()

        collection.features.forEach { feature ->
            val countryName = feature.properties.name ?: "Unknown"
            val countryId = feature.properties.isoCode
                ?.takeUnless { it == "-99" }
                ?: feature.properties.isoCodeFallback
                ?: countryName

            when (feature.geometry.type) {
                "Polygon" -> {
                    val boundary = parsePolygon(feature.geometry.coordinates)
                    if (boundary.isNotEmpty()) {
                        countries.add(Country(id = countryId, name = countryName, boundary = boundary))
                    }
                }
                "MultiPolygon" -> {
                    val multiPolygons = parseMultiPolygon(feature.geometry.coordinates)
                    multiPolygons.forEachIndexed { index, boundary ->
                        countries.add(
                            Country(
                                id = "${countryId}_$index",
                                name = countryName,
                                boundary = boundary
                            )
                        )
                    }
                }
            }
        }
        return countries
    }

    private fun parsePolygon(coordinates: JsonArray): List<LatLng> {
        val points = mutableListOf<LatLng>()
        val outerRing = coordinates.firstOrNull()?.jsonArray ?: return emptyList()

        for (point in outerRing) {
            val coord = point.jsonArray
            val lon = coord[0].jsonPrimitive.float
            val lat = coord[1].jsonPrimitive.float
            points.add(LatLng(latitude = lat, longitude = lon))
        }
        return points
    }

    private fun parseMultiPolygon(coordinates: JsonArray): List<List<LatLng>> {
        val list = mutableListOf<List<LatLng>>()
        for (polygon in coordinates) {
            val points = parsePolygon(polygon.jsonArray)
            if (points.isNotEmpty()) list.add(points)
        }
        return list
    }
}
