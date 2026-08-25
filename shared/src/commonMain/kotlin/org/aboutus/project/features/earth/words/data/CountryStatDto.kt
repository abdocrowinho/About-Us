package org.aboutus.project.features.earth.words.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CountryStatDto(
    @SerialName("out_country_code") val country_code: String,
    @SerialName("out_state") val state: String,
    @SerialName("out_count") val count: Long,
    @SerialName("out_percentage") val percentage: Double

)
