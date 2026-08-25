package org.aboutus.project.core.data

import org.aboutus.project.features.earth.words.data.CountryStatDto
import org.aboutus.project.features.earth.words.data.WordPayload

import kotlinx.coroutines.flow.Flow

interface SupaServices {
    suspend fun connect()
    fun listenToWordsStream(): Flow<WordPayload>
    suspend fun sendWord(payload: WordPayload)
    suspend fun fetchCountryStats(countryCode: String? = null): List<CountryStatDto>
    suspend fun disconnect()
}