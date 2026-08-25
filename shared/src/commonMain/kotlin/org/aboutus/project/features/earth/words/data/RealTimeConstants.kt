package org.aboutus.project.features.earth.words.data

object RealtimeConstants {
    const val GLOBE_CHANNEL_NAME = "globe_words_channel"
    const val EVENT_NEW_WORD = "new_word"

    object RpcFunctions {
        const val INCREMENT_COUNTRY_STAT = "increment_country_stat"
        const val GET_COUNTRY_STATS = "get_country_stats"
    }

    object RpcParams {
        const val PARAM_COUNTRY = "p_country"
        const val PARAM_STATE = "p_state"
        const val PARAM_TARGET_COUNTRY = "p_target_country"
    }
}
