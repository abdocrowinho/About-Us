package org.aboutus.project.core.domain

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.broadcastFlow
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import org.aboutus.project.core.data.SupaServices
import org.aboutus.project.features.earth.words.data.CountryStatDto
import org.aboutus.project.features.earth.words.data.MessageState
import org.aboutus.project.features.earth.words.data.RealtimeConstants
import org.aboutus.project.features.earth.words.data.WordPayload

class SupaServicesImpl(
    private val client: SupabaseClient
) : SupaServices {

    private val channel: RealtimeChannel = client.realtime.channel(RealtimeConstants.GLOBE_CHANNEL_NAME)

    override suspend fun connect() {
        channel.subscribe()
    }

    override fun listenToWordsStream(): Flow<WordPayload> {
        return channel.broadcastFlow(event = RealtimeConstants.EVENT_NEW_WORD)
    }

    override suspend fun sendWord(payload: WordPayload) {
        channel.broadcast(
            event = RealtimeConstants.EVENT_NEW_WORD,
            message = Json.encodeToJsonElement(payload).jsonObject
        )

        recordWordStat(countryCode = payload.countryCode, state = payload.state)
    }

    private suspend fun recordWordStat(countryCode: String, state: MessageState) {
        client.postgrest.rpc(
            function = RealtimeConstants.RpcFunctions.INCREMENT_COUNTRY_STAT,
            parameters = mapOf(
                RealtimeConstants.RpcParams.PARAM_COUNTRY to countryCode,
                RealtimeConstants.RpcParams.PARAM_STATE to state.value
            )
        )
    }

    override suspend fun fetchCountryStats(countryCode: String?): List<CountryStatDto> {
        val params = if (countryCode != null) {
            mapOf(RealtimeConstants.RpcParams.PARAM_TARGET_COUNTRY to countryCode)
        } else {
            emptyMap()
        }

        return client.postgrest
            .rpc(RealtimeConstants.RpcFunctions.GET_COUNTRY_STATS, parameters = params)
            .decodeList<CountryStatDto>()
    }

    override suspend fun disconnect() {
        client.realtime.removeChannel(channel)
    }
}