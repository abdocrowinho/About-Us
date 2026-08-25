package org.aboutus.project.features.earth.wave.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class WaveStatsDto(
    @SerialName("dominant_state") val dominantState: String,
    @SerialName("total_messages") val totalMessages: Long
)