package org.aboutus.project.features.earth.peresntation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.about_us.project.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.aboutus.project.features.earth.data.GeoJsonParser
import org.aboutus.project.features.earth.domain.GeoUtils
import org.aboutus.project.features.earth.domain.Projection3D
import org.aboutus.project.features.earth.domain.toAppCountryIdentity
import org.aboutus.project.features.about_us.peresntation.Map3DState
import org.aboutus.project.features.about_us.peresntation.GlobeScope
import org.aboutus.project.features.location.domain.UserLocationPreferences

class Map3DViewModel(
    private val locationPreferences: UserLocationPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(Map3DState())
    val state: StateFlow<Map3DState> = _state.asStateFlow()

    init {
        locationPreferences.location?.let { location ->
            _state.update {
                it.copy(
                    rotationX = -location.latitude.toFloat(),
                    rotationY = -location.longitude.toFloat()
                )
            }
        }
        loadRealCountriesData()
    }

    fun onDrag(dragAmountX: Float, dragAmountY: Float) {
        _state.update { currentState ->
            val newRotY = (currentState.rotationY + dragAmountX * 0.4f) % 360f
            val newRotX = (currentState.rotationX - dragAmountY * 0.4f) % 360f

            val centerLatLng = GeoUtils.getCenterLatLng(newRotX, newRotY)

            val matchedCountry = currentState.countries.firstOrNull { country ->
                GeoUtils.isPointInPolygon(centerLatLng, country.boundary)
            }

            val identity = matchedCountry?.toAppCountryIdentity()
            currentState.copy(
                rotationX = newRotX,
                rotationY = newRotY,
                selectedCountryName = identity?.name,
                selectedCountryCode = identity?.code
            )
        }
    }

    fun onZoom(zoomFactor: Float) {
        _state.update { currentState ->
            currentState.copy(
                globeRadius = (currentState.globeRadius * zoomFactor)
                    .coerceIn(MIN_GLOBE_RADIUS, MAX_GLOBE_RADIUS)
            )
        }
    }

    fun setScope(scope: GlobeScope) {
        _state.update {
            if (scope == GlobeScope.SELECTED_COUNTRY && it.selectedCountryCode == null) it
            else it.copy(scope = scope)
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    private fun loadRealCountriesData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bytes = Res.readBytes("files/countries.geojson")
                val jsonString = bytes.decodeToString()

                val rawCountries = GeoJsonParser.parseCountries(jsonString)

                val currentRadius = _state.value.globeRadius
                val countries3D = rawCountries.map { country ->
                    country.copy(
                        points3D = country.boundary.map { latLng ->
                            Projection3D.latLngToVector3D(latLng, currentRadius)
                        }
                    )
                }

                _state.update { current ->
                    val center = GeoUtils.getCenterLatLng(current.rotationX, current.rotationY)
                    val matched = countries3D.firstOrNull { GeoUtils.isPointInPolygon(center, it.boundary) }
                    val identity = matched?.toAppCountryIdentity()
                    current.copy(
                        countries = countries3D,
                        selectedCountryName = identity?.name,
                        selectedCountryCode = identity?.code
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

private const val MIN_GLOBE_RADIUS = 125f
private const val MAX_GLOBE_RADIUS = 360f
