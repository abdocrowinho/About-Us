package org.aboutus.project.features.about_us.peresntation
import org.aboutus.project.features.earth.domain.Country

enum class GlobeScope { GLOBAL, SELECTED_COUNTRY }

data class Map3DState(
    val rotationX: Float = 15f,
    val rotationY: Float = 0f,
    val globeRadius: Float = 180f,
    val countries: List<Country> = emptyList(),
    val selectedCountryName: String? = null,
    val selectedCountryCode: String? = null,
    val scope: GlobeScope = GlobeScope.GLOBAL
)
