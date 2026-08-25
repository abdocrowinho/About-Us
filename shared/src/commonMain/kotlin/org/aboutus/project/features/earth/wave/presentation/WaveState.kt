package org.aboutus.project.features.earth.wave.presentation


import androidx.compose.ui.graphics.Color
import org.aboutus.project.features.earth.words.data.CountryStatDto
import org.aboutus.project.features.earth.domain.EarthError

data class WaveState(
    val intensity: Float = 0.5f,
    val waveCase: Color = Color.Red,
    val speedFactor: Float = 1f,
    val amplitudeFactor: Float = 20f,
    val scopeCountryCode: String? = null,
    val stats: List<CountryStatDto> = emptyList(),
    val globalStats: List<CountryStatDto> = emptyList(),
    val totalVotes: Long = 0,
    val participatingCountries: Int = 0,
    val dominantState: String? = null,
    val statsError: EarthError? = null,
    val isLoading: Boolean = false
)
